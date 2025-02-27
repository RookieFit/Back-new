package com.rookiefit.rookiefit.auth

import com.rookiefit.rookiefit.auth.dto.ResponseDTO
import com.rookiefit.rookiefit.auth.dto.request.SignInRequestDTO
import com.rookiefit.rookiefit.auth.dto.request.SignUpRequestDTO
import com.rookiefit.rookiefit.provider.JwtProvider
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
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
    private val log = LoggerFactory.getLogger(UserService::class.java)

    fun getRefreshTokenFromCookie(request: HttpServletRequest): String? {
        val cookies = request.cookies
        if (cookies != null) {
            // 쿠키 배열을 순회하여 name과 value 출력
            cookies.forEach { cookie ->
                log.debug("Cookie Name: {}", cookie.name)
            }
        } else {
            log.debug("No cookies found")
        }
        return cookies?.find { it.name == "refreshToken" }?.value
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

    fun signIn(signInRequestDTO: SignInRequestDTO, response: HttpServletResponse): String? {
        val user = userRepository.findByUserId(signInRequestDTO.userId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자")
        val currentPassword = signInRequestDTO.userPassword
        val encodedPassword = user.userPassword
        val isMatch = passwordEncoder.matches(currentPassword, encodedPassword)
        if(!isMatch) throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증실패")

        val accessToken = jwtProvider.generateAccessToken(user.userId)
        val refreshToken = jwtProvider.generateRefreshToken(user.userId)

        //todo: https연결후 secure값 true로 변경할것
        val refreshTokenCookie = Cookie("refreshToken", refreshToken).apply {
            isHttpOnly = true
            secure = false  // HTTPS 환경에서만 사용하려면 true로 설정
            path = "/"  // 모든 경로에서 쿠키 사용
            maxAge = 7 * 24 * 60 * 60  // 일주일 유효
        }
        response.addCookie(refreshTokenCookie)
        return accessToken
    }
}