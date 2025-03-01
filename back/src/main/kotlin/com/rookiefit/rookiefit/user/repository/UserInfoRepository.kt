package com.rookiefit.rookiefit.user.repository

import com.rookiefit.rookiefit.user.entity.UserInfoEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserInfoRepository: JpaRepository<UserInfoEntity, Long> {
    fun findByUserProfile_UserProfileId(userProfileId: Long): List<UserInfoEntity>?
    fun findByUserProfile_UserProfileIdAndUserInfoInbodyDate(
        userProfileId: Long,
        userInfoInBodyDate: String
    ): List<UserInfoEntity>?
}