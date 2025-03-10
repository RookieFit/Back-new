package com.rookiefit.rookiefit.community.service

import com.rookiefit.rookiefit.auth.dto.ResponseDTO
import com.rookiefit.rookiefit.common.DateFormat
import com.rookiefit.rookiefit.community.dto.CommunityRequestDTO
import com.rookiefit.rookiefit.community.entity.CommunityEntity
import com.rookiefit.rookiefit.community.repository.CommunityRepository
import com.rookiefit.rookiefit.user.repository.UserProfileRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class CommunityService (
    private val profileRepository: UserProfileRepository,
    private val communityRepository: CommunityRepository
){
    fun createCommunity(currentUserId: String?, communityRequestDTO: CommunityRequestDTO): ResponseDTO{
        val userProfileEntity = profileRepository.findByUser_UserId(currentUserId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND,"사용자를 찾을수 없습니다")
        val communityEntity = CommunityEntity(
            communityTitle = communityRequestDTO.communityTitle,
            communityContent = communityRequestDTO.communityContent,
            communityType = communityRequestDTO.communityType,
            communityAuthor = userProfileEntity.userProfileNickname,
            communityCreatedAt = DateFormat.now(),
            userProfile = userProfileEntity,
        )
        communityRepository.save(communityEntity)
        return ResponseDTO("COMMUNITY_CREATE_SUCCESS", "게시글이 저장되었습니다")
    }
}