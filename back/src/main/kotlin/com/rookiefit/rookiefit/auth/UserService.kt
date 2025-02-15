package com.rookiefit.rookiefit.auth

import com.rookiefit.rookiefit.auth.dto.ResponseDTO
import com.rookiefit.rookiefit.auth.dto.request.SignInRequestDTO
import com.rookiefit.rookiefit.auth.dto.request.SignUpRequestDTO
import com.rookiefit.rookiefit.auth.dto.response.SignInResponseDTO
import com.rookiefit.rookiefit.provider.JwtProvider
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val jwtProvider: JwtProvider
) {
    private val passwordEncoder = BCryptPasswordEncoder()

    fun idCheck(userId: String): Boolean{
        val isExisted = userRepository.existsByUserId(userId)
        return !isExisted
    }

    fun signUp(signUpRequestDTO: SignUpRequestDTO): ResponseDTO {
        val isExisted = userRepository.existsByUserId(signUpRequestDTO.userId)
        if(isExisted)return ResponseDTO("ID_DUPLICATED", "중복된 아이디입니다")
        val encryptedPassword = passwordEncoder.encode(signUpRequestDTO.userPassword)
        val userEntity = UserEntity(
            userId = signUpRequestDTO.userId,
            userPassword = encryptedPassword,
            userPhoneNumber = signUpRequestDTO.userPhoneNumber,
            role = "ROLE_USER",
            type = "app"
        )
        userRepository.save(userEntity)
        return ResponseDTO("SIGN_UP_SUCCESS", "회원가입 성공")
    }
    fun signIn(signInRequestDTO: SignInRequestDTO): SignInResponseDTO? {
        val user = userRepository.findByUserId(signInRequestDTO.userId) ?: throw IllegalArgumentException("존재하지 않는 사용자")
        val currentPassword = signInRequestDTO.userPassword
        val encodedPassword = user.userPassword
        println("$user/$currentPassword")
        val isMatch = passwordEncoder.matches(currentPassword, encodedPassword)
        if(isMatch){
            val accessToken = jwtProvider.generateAccessToken(user.userId)
            val refreshToken = jwtProvider.generateRefreshToken(user.userId)
            return SignInResponseDTO(accessToken, refreshToken)
        }
        return null
    }
}