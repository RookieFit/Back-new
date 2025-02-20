package com.rookiefit.rookiefit.auth

import com.rookiefit.rookiefit.auth.dto.ResponseDTO
import com.rookiefit.rookiefit.auth.dto.request.RefreshTokenRequest
import com.rookiefit.rookiefit.auth.dto.request.SignInRequestDTO
import com.rookiefit.rookiefit.auth.dto.request.SignUpRequestDTO
import com.rookiefit.rookiefit.auth.dto.response.SignInResponseDTO
import org.apache.coyote.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class UserController(private val userService: UserService) {
    @PostMapping("/refresh-token")
    fun refreshToken(@RequestBody refreshTokenRequest: RefreshTokenRequest): ResponseEntity<SignInResponseDTO> {
        val responseBody = userService.refreshToken(refreshTokenRequest.refreshToken)
        return ResponseEntity.status(HttpStatus.OK).body(responseBody)
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
    fun signIn(@RequestBody signInRequestDTO: SignInRequestDTO): ResponseEntity<SignInResponseDTO>{
        val response = userService.signIn(signInRequestDTO)
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }
}