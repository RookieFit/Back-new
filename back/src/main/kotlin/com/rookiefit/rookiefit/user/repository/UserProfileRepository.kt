package com.rookiefit.rookiefit.user.repository

import com.rookiefit.rookiefit.user.entity.UserProfileEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserProfileRepository: JpaRepository<UserProfileEntity, Long> {
    fun findByUser_UserId(userId: String?): UserProfileEntity?
}