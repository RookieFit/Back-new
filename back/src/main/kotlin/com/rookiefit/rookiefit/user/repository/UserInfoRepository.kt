package com.rookiefit.rookiefit.user.repository

import com.rookiefit.rookiefit.user.entity.UserInfoEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserInfoRepository: JpaRepository<UserInfoEntity, Long> {
    fun findByUserProfile_UserProfileId(userProfileId: Long): List<UserInfoEntity>?
    fun findByUserInfoInbodyDate( userInfoInBodyDate: String): UserInfoEntity?
}