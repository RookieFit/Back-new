package com.rookiefit.rookiefit.auth

import com.rookiefit.rookiefit.auth.dto.ResponseDTO
import com.rookiefit.rookiefit.auth.dto.request.SignInRequestDTO
import com.rookiefit.rookiefit.auth.dto.request.SignUpRequestDTO
import com.rookiefit.rookiefit.auth.dto.response.SignInResponseDTO
import com.rookiefit.rookiefit.provider.JwtProvider
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class UserService(
    private val userRepository: UserRepository,
    private val jwtProvider: JwtProvider
) {
    private val passwordEncoder = BCryptPasswordEncoder()

    fun refreshToken(refreshToken: String): SignInResponseDTO? {
        if(jwtProvider.validateToken(refreshToken)) {
            val userId = jwtProvider.extractUserId(refreshToken)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자")
            val newAccessToken = jwtProvider.generateAccessToken(userId)
            val newRefreshToken = jwtProvider.generateRefreshToken(userId)
            return SignInResponseDTO(newAccessToken, newRefreshToken)
        }
        throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }

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
        val user = userRepository.findByUserId(signInRequestDTO.userId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자")
        val currentPassword = signInRequestDTO.userPassword
        val encodedPassword = user.userPassword
        val isMatch = passwordEncoder.matches(currentPassword, encodedPassword)
        if(!isMatch) throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증실패")

        val accessToken = jwtProvider.generateAccessToken(user.userId)
        val refreshToken = jwtProvider.generateRefreshToken(user.userId)
        return SignInResponseDTO(accessToken, refreshToken)
    }
}