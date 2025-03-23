package com.rookiefit.rookiefit.user.controller

import com.rookiefit.rookiefit.auth.dto.ResponseDTO
import com.rookiefit.rookiefit.user.dto.UserInfoDTO
import com.rookiefit.rookiefit.user.dto.UserProfileDTO
import com.rookiefit.rookiefit.user.dto.response.*
import com.rookiefit.rookiefit.user.service.UserDataService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/user/userdata")
class UserDataController(
    private val userDataService: UserDataService
) {
    @PostMapping(value = ["/createprofile"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun updateUserData(
        @RequestPart("profile") userProfileDTO: UserProfileDTO,
        @RequestPart("profileimage", required = false) userProfileImage: MultipartFile?
    ): ResponseEntity<ResponseDTO> {
        val authentication = SecurityContextHolder.getContext().authentication
        val currentUserId = authentication?.principal as String
        val responseBody = userDataService.updateUserData(currentUserId, userProfileDTO, userProfileImage)
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody)
    }

    @PostMapping("/createuserinfo")
    fun createUserInfo(@RequestBody userInfoDTO: UserInfoDTO): ResponseEntity<ResponseDTO> {
        val authentication = SecurityContextHolder.getContext().authentication
        val currentUserId = authentication?.principal as String
        val responseBody = userDataService.createUserInfo(userInfoDTO, currentUserId)
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody)
    }

    @GetMapping("/getprofile")
    fun getUserProfile(): UserProfileResponseDTO? {
        val authentication = SecurityContextHolder.getContext().authentication
        val currentUserId = authentication?.principal as String
        val responseBody = userDataService.getUserProfile(currentUserId)
        return responseBody
    }

    @GetMapping("/getuserinfo")
    fun getUserInfo(): UserInfoResponseDTO? {
        val authentication = SecurityContextHolder.getContext().authentication
        val currentUserId = authentication?.principal as String
        val responseBody = userDataService.getUserInfo(currentUserId)
        return responseBody
    }

    @GetMapping("/getuserweightdata")
    fun getUserWeightData(): List<UserWeightResponseDTO> {
        val authentication = SecurityContextHolder.getContext().authentication
        val currentUserId = authentication?.principal as String
        val responseBody = userDataService.getUserWeightData(currentUserId)
        return responseBody
    }

    @GetMapping("/getusermuscledata")
    fun getuserMuscledata(): List<UserMuscleResponseDTO> {
        val authentication = SecurityContextHolder.getContext().authentication
        val currentUserId = authentication?.principal as String
        val responseBody = userDataService.getuserMuscledata(currentUserId)
        return responseBody
    }

    @GetMapping("/getuserfatdata")
    fun getuserFatdata(): List<UserFatResponseDTO> {
        val authentication = SecurityContextHolder.getContext().authentication
        val currentUserId = authentication?.principal as String
        val responseBody = userDataService.getuserFatdata(currentUserId)
        return responseBody
    }
}