package com.rookiefit.rookiefit.auth.dto.request

import jakarta.validation.constraints.NotEmpty

data class SignUpRequestDTO(
    @NotEmpty
    val userId: String,
    @NotEmpty
    val userPassword: String,
    val userPhoneNumber: String
)