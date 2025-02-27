package com.rookiefit.rookiefit.auth

import com.rookiefit.rookiefit.auth.dto.ResponseDTO
import com.rookiefit.rookiefit.auth.dto.request.SignInRequestDTO
import com.rookiefit.rookiefit.auth.dto.request.SignUpRequestDTO
import com.rookiefit.rookiefit.provider.JwtProvider
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/auth")
class UserController(
    private val userService: UserService,
    private val jwtProvider: JwtProvider
) {
    @PostMapping("/refresh")
    fun refreshToken(request: HttpServletRequest): String {
        val refreshToken = userService.getRefreshTokenFromCookie(request)
        if (refreshToken == null) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 리프레시 토큰")
        }

        val userId = jwtProvider.extractUserId(refreshToken)
        println("userid=$userId")
        val accessToken = jwtProvider.generateAccessToken(userId)

        println("newAccessToken=$accessToken")
        return accessToken
    }

    @GetMapping("/id-check")
    fun idCheck(@RequestParam userId: String): Boolean {
        return userService.idCheck(userId)
    }
    @PostMapping("/sign-up")
    fun signUp(@RequestBody signUpRequestDTO: SignUpRequestDTO): ResponseEntity<ResponseDTO>{
        val response = userService.signUp(signUpRequestDTO)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }
    @PostMapping("/sign-in")
    fun signIn(@RequestBody signInRequestDTO: SignInRequestDTO, response: HttpServletResponse): String?{
        val accessToken = userService.signIn(signInRequestDTO, response)
        return accessToken
    }
}