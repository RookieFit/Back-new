package com.rookiefit.rookiefit.testcontroller

import com.rookiefit.rookiefit.provider.JwtProvider
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

data class AuthRequest(val userId: String)

@RestController
class AuthController(private val jwtProvider: JwtProvider) {
    @PostMapping("/api/auth/login")
    fun login(@RequestBody authRequest: AuthRequest): Map<String, String> {
        val token = jwtProvider.generateAccessToken(authRequest.userId)
        return mapOf("Token" to token)
    }
}