package com.rookiefit.rookiefit.community.service

import com.rookiefit.rookiefit.auth.dto.ResponseDTO
import com.rookiefit.rookiefit.common.DateFormat
import com.rookiefit.rookiefit.community.dto.request.CommunityRequestDTO
import com.rookiefit.rookiefit.community.dto.response.CommunityDetailResponseDTO
import com.rookiefit.rookiefit.community.dto.response.CommunityResponseDTO
import com.rookiefit.rookiefit.community.entity.CommunityEntity
import com.rookiefit.rookiefit.community.repository.CommunityRepository
import com.rookiefit.rookiefit.user.repository.UserProfileRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
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
    fun getCommunityList(communityType: String, page: Int, size: Int): Map<String, Any> {
        val pageable: Pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("communityCreatedAt")))
        val communityPage: Page<CommunityEntity> = if (communityType == "전체") {
            communityRepository.findAll(pageable)
        }else{
            communityRepository.findByCommunityType(communityType, pageable)
        }
        val communityResponseDTOList = communityPage.content.map {
            CommunityResponseDTO(
                communityId = it.communityId,
                communityType = it.communityType,
                communityTitle = it.communityTitle,
                communityAuthor = it.communityAuthor,
                communityCreatedAt = it.communityCreatedAt
            )
        }
        return mapOf(
            "content" to communityResponseDTOList,
            "totalPages" to communityPage.totalPages
        )
    }
    fun getMyCommunityList(currentUserId: String?, communityType: String, page: Int, size: Int): Map<String, Any> {
        val userProfileEntity = profileRepository.findByUser_UserId(currentUserId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저의 프로필이 존재하지 않습니다")
        val pageable: Pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("communityCreatedAt")))
        val communityPage: Page<CommunityEntity> = if (communityType == "전체") {
            communityRepository.findByUserProfile_UserProfileId(userProfileEntity.userProfileId, pageable)
        }else{
            communityRepository.findByUserProfile_UserProfileIdAndCommunityType(
                userProfileEntity.userProfileId,
                communityType,
                pageable
            )
        }
        val communityResponseDTOList = communityPage.content.map {
            CommunityResponseDTO(
                communityId = it.communityId,
                communityType = it.communityType,
                communityTitle = it.communityTitle,
                communityAuthor = it.communityAuthor,
                communityCreatedAt = it.communityCreatedAt
            )
        }
        return mapOf(
            "content" to communityResponseDTOList,
            "totalPages" to communityPage.totalPages
        )
    }
    fun getCommunityDetail(communityId: Long): CommunityDetailResponseDTO {
        val communityEntity = communityRepository.findByCommunityId(communityId)
        return CommunityDetailResponseDTO(
            communityTitle = communityEntity.communityTitle,
            communityContent = communityEntity.communityContent,
            communityAuthor = communityEntity.communityAuthor,
            communityCreatedAt = communityEntity.communityCreatedAt,
            communityType = communityEntity.communityType
        )
    }
}