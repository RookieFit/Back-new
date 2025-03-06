package com.rookiefit.rookiefit.diet.dto

import com.rookiefit.rookiefit.diet.entity.UserDietEntity

data class UserDietDto(
    val id: Long,
    val userId: String,
    val dietDate: String,
    val totalCalories: Double?,
    val dietDetails: List<UserDietDetailDto>
) {
    companion object {
        fun fromEntity(entity: UserDietEntity): UserDietDto {
            return UserDietDto(
                id = entity.id,
                userId = entity.userId,
                dietDate = entity.dietDate,
                totalCalories = entity.totalCalories,
                dietDetails = entity.details.map { UserDietDetailDto.fromEntity(it) }
            )
        }
    }
}
