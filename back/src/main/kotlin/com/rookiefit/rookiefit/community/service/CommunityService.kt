package com.rookiefit.rookiefit.community.service

import com.rookiefit.rookiefit.auth.dto.ResponseDTO
import com.rookiefit.rookiefit.common.DateFormat
import com.rookiefit.rookiefit.common.FirebaseService
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
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class CommunityService (
    private val profileRepository: UserProfileRepository,
    private val communityRepository: CommunityRepository,
    private val firebaseService: FirebaseService
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
    fun getCommunityList(
        communityType: String,
        page: Int,
        size: Int,
        searchType: String?,
        searchQuery: String?
    ): Map<String, Any> {
        val pageable: Pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("communityCreatedAt")))
        val communityPage: Page<CommunityEntity> = when {
            communityType == "전체" && searchQuery != null && searchType != null -> {
                when (searchType) {
                    "제목만" -> communityRepository.findByCommunityTitleContaining(searchQuery, pageable)
                    "글작성자" -> communityRepository.findByCommunityAuthorContaining(searchQuery, pageable)
                    else -> communityRepository.findAll(pageable)
                }
            }
            communityType != "전체" && searchQuery != null && searchType != null -> {
                when (searchType) {
                    "제목만" -> communityRepository.findByCommunityTypeAndCommunityTitleContaining(communityType, searchQuery, pageable)
                    "글작성자" -> communityRepository.findByCommunityTypeAndCommunityAuthorContaining(communityType, searchQuery, pageable)
                    else -> communityRepository.findByCommunityType(communityType, pageable)
                }
            }
            communityType == "전체" -> {
                communityRepository.findAll(pageable)
            }
            else -> {
                communityRepository.findByCommunityType(communityType, pageable)
            }
        }
        val communityResponseDTOList = communityPage.content.map {
            CommunityResponseDTO(
                communityId = it.communityId,
                communityType = it.communityType,
                communityTitle = it.communityTitle,
                communityAuthor = it.communityAuthor,
                communityCreatedAt = it.communityCreatedAt,
                communityUpdatedAt = it.communityUpdatedAt?: "",
            )
        }
        return mapOf(
            "content" to communityResponseDTOList,
            "totalPages" to communityPage.totalPages
        )
    }
    fun getMyCommunityList(
        currentUserId: String?,
        communityType: String,
        page: Int, size: Int,
        searchType: String?,
        searchQuery: String?
    ): Map<String, Any> {
        val userProfileEntity = profileRepository.findByUser_UserId(currentUserId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저의 프로필이 존재하지 않습니다")
        val pageable: Pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("communityCreatedAt")))
        val communityPage: Page<CommunityEntity> = when {
            communityType == "전체" && searchQuery != null && searchType != null -> {
                when (searchType) {
                    "제목만" -> communityRepository.findByUserProfile_UserProfileIdAndCommunityTitleContaining(userProfileEntity.userProfileId, searchQuery, pageable)
                    "글작성자" -> communityRepository.findByUserProfile_UserProfileIdAndCommunityAuthorContaining(userProfileEntity.userProfileId, searchQuery, pageable)
                    else -> communityRepository.findByUserProfile_UserProfileId(userProfileEntity.userProfileId, pageable)
                }
            }
            communityType != "전체" && searchQuery != null && searchType != null -> {
                when (searchType) {
                    "제목만" -> communityRepository.findByUserProfile_UserProfileIdAndCommunityTypeAndCommunityTitleContaining(
                        userProfileEntity.userProfileId,
                        communityType,
                        searchQuery,
                        pageable)
                    "글작성자" -> communityRepository.findByUserProfile_UserProfileIdAndCommunityTypeAndCommunityAuthorContaining(
                        userProfileEntity.userProfileId,
                        communityType,
                        searchQuery,
                        pageable)
                    else -> communityRepository.findByUserProfile_UserProfileIdAndCommunityType(userProfileEntity.userProfileId, communityType, pageable)
                }
            }
            communityType == "전체" -> {
                communityRepository.findByUserProfile_UserProfileId(userProfileEntity.userProfileId, pageable)
            }
            else -> {
                communityRepository.findByUserProfile_UserProfileIdAndCommunityType(userProfileEntity.userProfileId, communityType, pageable)
            }
        }
        val communityResponseDTOList = communityPage.content.map {
            CommunityResponseDTO(
                communityId = it.communityId,
                communityType = it.communityType,
                communityTitle = it.communityTitle,
                communityAuthor = it.communityAuthor,
                communityCreatedAt = it.communityCreatedAt,
                communityUpdatedAt = it.communityUpdatedAt?:""
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
            communityType = communityEntity.communityType,
            userProfileId = communityEntity.userProfile?.userProfileId ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "작성자가 존재하지 않습니다."),
            communityUpdatedAt = communityEntity.communityUpdatedAt?:"",
        )
    }
    @Transactional
    fun updateCommunity(currentUserId: String?, communityId: Long, communityRequestDTO: CommunityRequestDTO): ResponseEntity<ResponseDTO> {
        val userProfileEntity = profileRepository.findByUser_UserId(currentUserId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND,"사용자를 찾을수 없습니다")
        val communityEntity = communityRepository.findByCommunityId(communityId)
        val oldImages = firebaseService.extractImageUrls(communityEntity.communityContent)
        communityEntity.communityTitle = communityRequestDTO.communityTitle
        communityEntity.communityContent = communityRequestDTO.communityContent
        communityEntity.communityType = communityRequestDTO.communityType
        communityEntity.communityAuthor = userProfileEntity.userProfileNickname
        communityEntity.communityUpdatedAt = DateFormat.now()
        val newImages = firebaseService.extractImageUrls(communityRequestDTO.communityContent)
        val imagesToDelete = oldImages - newImages
        firebaseService.deleteImageFiles(imagesToDelete.toList())
        return ResponseEntity.ok(ResponseDTO("UPDATE_COMMUNITY_SUCCESS", "게시글이 수정되었습니다"))
    }
    @Transactional
    fun deleteCommunity(communityId: Long, currentUserId: String?): ResponseEntity<ResponseDTO> {
        val userProfileEntity = profileRepository.findByUser_UserId(currentUserId)
            ?:throw ResponseStatusException(HttpStatus.NOT_FOUND, "유저 정보가 존재하지 않습니다")
        val communityEntity = communityRepository.findByCommunityId(communityId)
        if(communityEntity.userProfile?.userProfileId != userProfileEntity.userProfileId) {
            return ResponseEntity.badRequest().body(ResponseDTO("NOT_MATCH_USER", "작성자만 삭제가 가능합니다"))
        }else {
            val imagesToDelete = firebaseService.extractImageUrls(communityEntity.communityContent)
            firebaseService.deleteImageFiles(imagesToDelete.toList())
            communityRepository.delete(communityEntity)
            return ResponseEntity.ok(ResponseDTO("DELETE_COMMUNITY_SUCCESS", "게시글이 삭제되었습니다"))
        }
    }
}