package com.rookiefit.rookiefit.user.controller

import com.rookiefit.rookiefit.auth.dto.ResponseDTO
import com.rookiefit.rookiefit.user.dto.UserProfileDTO
import com.rookiefit.rookiefit.user.dto.response.UserProfileResponseDTO
import com.rookiefit.rookiefit.user.service.UserDataService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/user/userdata")
class UserDataController(
    private val userDataService: UserDataService
) {
    @PostMapping("/createprofile")
    fun createUserData(
        @RequestPart userProfileDTO: UserProfileDTO,
        @RequestPart userProfileImage: MultipartFile
    ): ResponseEntity<ResponseDTO> {
        val authentication = SecurityContextHolder.getContext().authentication
        val currentUserId = authentication?.principal as? String
        val responseBody = userDataService.createUserData(currentUserId, userProfileDTO, userProfileImage)
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody)
    }

    @GetMapping("/getprofile")
    fun getUserProfile(): UserProfileResponseDTO? {
        val authentication = SecurityContextHolder.getContext().authentication
        val currentUserId = authentication?.principal as? String
        val responseBody = userDataService.getUserProfile(currentUserId)
        return responseBody
    }
}