package com.rookiefit.rookiefit.auth

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, String>{
    fun findByUserId(userId: String): List<UserEntity>
}