package com.rookiefit.rookiefit.auth.repository

import com.rookiefit.rookiefit.auth.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, String>{
    fun existsByUserId(userId: String) : Boolean
    fun findByUserId(userId: String): UserEntity?
}
