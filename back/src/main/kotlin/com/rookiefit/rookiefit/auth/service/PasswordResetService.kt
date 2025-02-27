package com.rookiefit.rookiefit.auth.service

import com.rookiefit.rookiefit.auth.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class PasswordResetService(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder
) {
    fun resetPassword(userId: String, newPassword: String): String {
        val user = userRepository.findByUserId(userId)
            ?: throw IllegalArgumentException("해당 아이디로 등록된 사용자가 없습니다.")

        // 새 비밀번호를 암호화하여 저장
        user.userPassword = passwordEncoder.encode(newPassword)
        userRepository.save(user)

        return "비밀번호가 성공적으로 변경되었습니다."
    }
}
