package com.rookiefit.rookiefit.user.dto

import com.rookiefit.rookiefit.user.entity.UserProfileEntity

data class UserProfileDTO(
    val userProfileNickname: String,
    val userProfileAddress: String,
    val userProfileMessage: String,
    val userProfileGymName: String,
)
