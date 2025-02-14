package com.rookiefit.rookiefit.auth

import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {
    fun idCheck(userId: String): Boolean {
        val currentUserId = userRepository.findByUserId(userId)
        return currentUserId.isNotEmpty()
    }
}