package com.rookiefit.rookiefit.community.controller

import com.rookiefit.rookiefit.auth.dto.ResponseDTO
import com.rookiefit.rookiefit.common.FirebaseService
import com.rookiefit.rookiefit.community.dto.request.CommunityRequestDTO
import com.rookiefit.rookiefit.community.dto.response.CommunityDetailResponseDTO
import com.rookiefit.rookiefit.community.service.CommunityService
import com.rookiefit.rookiefit.user.repository.UserProfileRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/user/community")
class CommunityController(
    private val firebaseService: FirebaseService,
    private val communityService: CommunityService,
    private val profileRepository: UserProfileRepository
) {
    @PostMapping("/uploadContentImages")
    fun uploadContentImages(
        @RequestPart("contentImageFiles", required = false) contentImages: List<MultipartFile>?
    ): List<String>?{
        if(contentImages.isNullOrEmpty()) {
            println("file not found")
            return null
        }
        return firebaseService.uploadImageFiles(contentImages)
    }
    @GetMapping("/getProfileId")
    fun getProfileId(): Long {
        val authentication = SecurityContextHolder.getContext().authentication
        val currentUserId = authentication?.principal as? String
        val userProfileEntity = profileRepository.findByUser_UserId(currentUserId)
            ?:throw ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저의 프로필이 존재하지 않습니다")
        return userProfileEntity.userProfileId
    }
    @PostMapping("/createCommunity")
    fun createCommunity(@RequestBody communityRequestDTO: CommunityRequestDTO): ResponseEntity<ResponseDTO>{
        val authentication = SecurityContextHolder.getContext().authentication
        val currentUserId = authentication?.principal as? String
        val responseBody = communityService.createCommunity(currentUserId, communityRequestDTO)
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody)
    }
    @GetMapping("/list")
    fun getCommunityList(
        @RequestParam(defaultValue = "전체") communityType: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) searchType: String?,
        @RequestParam(required = false) searchQuery: String?
    ): Map<String, Any> {
        return communityService.getCommunityList(communityType, page, size, searchType, searchQuery)
    }
    @GetMapping("/mylist")
    fun getMyCommunityList(
        @RequestParam(defaultValue = "전체") communityType: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) searchType: String?,
        @RequestParam(required = false) searchQuery: String?
    ): Map<String, Any> {
        val authentication = SecurityContextHolder.getContext().authentication
        val currentUserId = authentication?.principal as? String
        return communityService.getMyCommunityList(currentUserId, communityType, page, size, searchType, searchQuery)
    }
    @GetMapping("/detail")
    fun getCommunityDetail(@RequestParam communityId: Long): CommunityDetailResponseDTO {
        return communityService.getCommunityDetail(communityId)
    }
    @PutMapping("/updateCommunity")
    fun updateCommunity(
        @RequestParam communityId: Long,
        @RequestBody communityRequestDTO: CommunityRequestDTO
    ): ResponseEntity<ResponseDTO> {
        val authentication = SecurityContextHolder.getContext().authentication
        val currentUserId = authentication?.principal as? String
        val responseBody = communityService.updateCommunity(currentUserId, communityId, communityRequestDTO)
        return responseBody
    }
    @DeleteMapping("/deleteCommunity")
    fun deleteCommunity(@RequestParam communityId: Long): ResponseEntity<ResponseDTO> {
        val authentication = SecurityContextHolder.getContext().authentication
        val currentUserId = authentication?.principal as? String
        val responseBody = communityService.deleteCommunity(communityId, currentUserId);
        return responseBody
    }
}