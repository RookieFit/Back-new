package com.rookiefit.rookiefit.auth

import jakarta.annotation.Nonnull
import jakarta.validation.constraints.NotEmpty

data class UserDTO(
    @NotEmpty
    val userId: String,
    @NotEmpty
    val userPassword: String,
    val userPhoneNumber: String,
)