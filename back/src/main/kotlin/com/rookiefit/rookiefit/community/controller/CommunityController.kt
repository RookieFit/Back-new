package com.rookiefit.rookiefit.community.controller

import com.rookiefit.rookiefit.auth.dto.ResponseDTO
import com.rookiefit.rookiefit.common.FirebaseService
import com.rookiefit.rookiefit.community.dto.CommunityRequestDTO
import com.rookiefit.rookiefit.community.service.CommunityService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/user/community")
class CommunityController(
    private val firebaseService: FirebaseService,
    private val communityService: CommunityService
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
    @PostMapping("/createCommunity")
    fun createCommunity(@RequestBody communityRequestDTO: CommunityRequestDTO): ResponseEntity<ResponseDTO>{
        val authentication = SecurityContextHolder.getContext().authentication
        val currentUserId = authentication?.principal as? String
        val responseBody = communityService.createCommunity(currentUserId, communityRequestDTO)
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody)
    }
}