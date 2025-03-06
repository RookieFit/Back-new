package com.rookiefit.rookiefit.diet.dto

import com.rookiefit.rookiefit.diet.entity.UserDietDetailEntity

data class UserDietDetailDto(
    val id: Long,
    val foodName: String,
    val foodFirstCategory: String,
    val chocdf: Double,
    val prot: Double,
    val fatce: Double,
    val enerc: Double
) {
    companion object {
        fun fromEntity(entity: UserDietDetailEntity): UserDietDetailDto {
            return UserDietDetailDto(
                id = entity.id,
                foodName = entity.foodName,
                foodFirstCategory = entity.foodFirstCategory,
                chocdf = entity.chocdf,
                prot = entity.prot,
                fatce = entity.fatce,
                enerc = entity.enerc
            )
        }
    }
}
