package com.rookiefit.rookiefit.auth

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {
    private val passwordEncoder = BCryptPasswordEncoder()
    fun idCheck(userId: String): Boolean {
        val currentUserId = userRepository.findByUserId(userId)
        return currentUserId.isNotEmpty()
    }
    fun signUp(userDTO: UserDTO): String {
        val encryptedPassword = passwordEncoder.encode(userDTO.userPassword)
        val userEntity = UserEntity(
            userId = userDTO.userId,
            userPassword = encryptedPassword,
            userPhoneNumber = userDTO.userPhoneNumber
        )
        userRepository.save(userEntity)
        return "회원가입 성공 service"
    }
}