package com.rookiefit.rookiefit.user.dto

data class UserInfoDTO(
    val userInfoAge: Int,
    val userInfoWeight: Double,
    val userInfoHeight: Double,
    val userInfoMuscleMass: Double,
    val userInfoFatMass: Double,
    val userInfoInBodyDate: String,
    val userBasalMetabolicRate: Double,
)
