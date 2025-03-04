package com.rookiefit.rookiefit.user.service

import com.rookiefit.rookiefit.auth.dto.ResponseDTO
import com.rookiefit.rookiefit.auth.repository.UserRepository
import com.rookiefit.rookiefit.common.FirebaseService
import com.rookiefit.rookiefit.user.dto.UserInfoDTO
import com.rookiefit.rookiefit.user.dto.UserProfileDTO
import com.rookiefit.rookiefit.user.dto.response.UserInfoResponseDTO
import com.rookiefit.rookiefit.user.dto.response.UserProfileResponseDTO
import com.rookiefit.rookiefit.user.entity.UserInfoEntity
import com.rookiefit.rookiefit.user.repository.UserInfoRepository
import com.rookiefit.rookiefit.user.repository.UserProfileRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class UserDataService(
    private val userRepository: UserRepository,
    private val userProfileRepository: UserProfileRepository,
    private val userInfoRepository: UserInfoRepository,
    private val firebaseService: FirebaseService
) {
    @Transactional
    fun updateUserData(
        currentUserId: String,
        userProfileDTO: UserProfileDTO,
        userProfileImage: MultipartFile?
    ): ResponseDTO {
        userRepository.findByUserId(currentUserId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자")

        val profileImageUri = if (userProfileImage != null) {
            firebaseService.uploadImageFile(userProfileImage) // 이미지 업로드
        } else {
            null // 이미지를 업로드하지 않으면 null
        }

        val userProfileEntity = userProfileRepository.findByUser_UserId(currentUserId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "프로필이 존재하지 않습니다")

        if (userProfileRepository.existsById(userProfileEntity.userProfileId)) {
            userProfileEntity.apply {
                userProfileNickname = userProfileDTO.userProfileNickname
                userProfileAddress = userProfileDTO.userProfileAddress
                userProfileMessage = userProfileDTO.userProfileMessage
                userProfileGymName = userProfileDTO.userProfileGymName
                userProfileName = userProfileDTO.userProfileName
                if (profileImageUri != null) {
                    userProfileImageUri = profileImageUri
                }
            }

            userProfileRepository.save(userProfileEntity)
        }
        return ResponseDTO("CREATE_PROFILE_SUCCESS", "유저 프로필 업데이트 성공")
    }

    fun createUserInfo(userInfoDTO: UserInfoDTO, currentUserId: String): ResponseDTO {
        userRepository.findByUserId(currentUserId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자")
        val userProfileEntity = userProfileRepository.findByUser_UserId(currentUserId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 프로필을 찾을 수 없습니다.")

        val existingUserInfo = userInfoRepository.findByUserInfoInbodyDate(
            userInfoDTO.userInfoInBodyDate
        )
        if (existingUserInfo != null) {
            existingUserInfo.apply{
                userInfoAge = userInfoDTO.userInfoAge
                userInfoWeight = userInfoDTO.userInfoWeight
                userInfoHeight = userInfoDTO.userInfoHeight
                userInfoFatMass = userInfoDTO.userInfoFatMass
                userInfoMuscleMass = userInfoDTO.userInfoMuscleMass
                userInfoBasalMetabolicRate = userInfoDTO.userBasalMetabolicRate
            }
            userInfoRepository.save(existingUserInfo)
            return ResponseDTO("UPDATE_USERINFO_SUCCESS", "유저 체성분 정보 업데이트 성공")
        }else {
            val newUserInfo = UserInfoEntity(
                userProfile = userProfileEntity,
                userInfoAge = userInfoDTO.userInfoAge,
                userInfoWeight = userInfoDTO.userInfoWeight,
                userInfoHeight = userInfoDTO.userInfoHeight,
                userInfoMuscleMass = userInfoDTO.userInfoMuscleMass,
                userInfoFatMass = userInfoDTO.userInfoFatMass,
                userInfoInbodyDate = userInfoDTO.userInfoInBodyDate,
                userInfoBasalMetabolicRate = userInfoDTO.userBasalMetabolicRate
            )
            userInfoRepository.save(newUserInfo)
            return ResponseDTO("CREATE_USERINFO_SUCCESS", "유저 체성분 생성")
        }
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
                userProfileName = it.userProfileName
            )
        }
    }

    fun getUserInfo(currentUserId: String?): UserInfoResponseDTO? {
        val userProfileEntity = userProfileRepository.findByUser_UserId(currentUserId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자")
        val userProfileId = userProfileEntity.userProfileId
        val userInfoEntityList = userInfoRepository.findByUserProfile_UserProfileId(userProfileId)
            ?: return null
        val latestUserInfo = userInfoEntityList.maxByOrNull {
            LocalDate.parse(it.userInfoInbodyDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }
        latestUserInfo ?: return null
        return UserInfoResponseDTO(
            userInfoAge = latestUserInfo.userInfoAge,
            userInfoWeight = latestUserInfo.userInfoWeight,
            userInfoHeight = latestUserInfo.userInfoHeight,
            userInfoMuscleMass = latestUserInfo.userInfoMuscleMass,
            userInfoFatMass = latestUserInfo.userInfoFatMass
        )
    }
}