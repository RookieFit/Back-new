package com.rookiefit.rookiefit.auth.dto.response

data class SignInResponseDTO(
    val accessToken: String,
    val refreshToken: String
)
