package com.rookiefit.rookiefit.auth.dto.request

import jakarta.validation.constraints.NotEmpty

data class SignInRequestDTO(
    @NotEmpty
    val userId: String,
    @NotEmpty
    val userPassword: String
)
