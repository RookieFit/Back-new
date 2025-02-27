package com.rookiefit.rookiefit.user.service

import com.rookiefit.rookiefit.auth.UserRepository
import com.rookiefit.rookiefit.auth.dto.ResponseDTO
import com.rookiefit.rookiefit.common.FirebaseService
import com.rookiefit.rookiefit.user.dto.UserProfileDTO
import com.rookiefit.rookiefit.user.dto.response.UserProfileResponseDTO
import com.rookiefit.rookiefit.user.entity.UserInfoEntity
import com.rookiefit.rookiefit.user.entity.UserProfileEntity
import com.rookiefit.rookiefit.user.repository.UserInfoRepository
import com.rookiefit.rookiefit.user.repository.UserProfileRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException

@Service
class UserDataService(
    private val userRepository: UserRepository,
    private val userProfileRepository: UserProfileRepository,
    private val userInfoRepository: UserInfoRepository,
    private val firebaseService: FirebaseService
) {
    fun createUserData(
        currentUserId: String?,
        userProfileDTO: UserProfileDTO,
        userProfileImage: MultipartFile
    ): ResponseDTO {
        userRepository.findByUserId(currentUserId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자")
        val profileImageUri = firebaseService.uploadImageFile(userProfileImage)
        val userProfileEntity = userProfileRepository.findByUser_UserId(currentUserId)
            ?: UserProfileEntity(user = userRepository.findByUserId(currentUserId)!!).apply {
                userProfileNickname = userProfileDTO.userProfileNickname
                userProfileAddress = userProfileDTO.userProfileAddress
                userProfileMessage = userProfileDTO.userProfileMessage
                userProfileGymName = userProfileDTO.userProfileGymName
                userProfileImageUri = profileImageUri
            }
        userProfileRepository.save(userProfileEntity)
        return ResponseDTO("CREATE_PROFILE_SUCCESS", "유저 프로필 생성")
    }

    fun getUserProfile(currentUserId: String?): UserProfileResponseDTO? {
        val userProfileEntity = userProfileRepository.findByUser_UserId(currentUserId)
        return userProfileEntity?.let {
            UserProfileResponseDTO(
                userProfileNickname = it.userProfileNickname,
                userProfileAddress = it.userProfileAddress,
                userProfileMessage = it.userProfileMessage,
                userProfileGymName = it.userProfileGymName,
                userProfileImageUri = it.userProfileImageUri,
            )
        }
    }
}