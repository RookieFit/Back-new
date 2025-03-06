package com.rookiefit.rookiefit.diet.dto

import com.rookiefit.rookiefit.diet.entity.FoodInfoEntity

data class FoodInfoDto(
    val id: Long,
    val foodName: String,
    val foodFirstCategory: String,
    val enerc: Double,
    val prot: Double,
    val fatce: Double,
    val chocdf: Double
) {
    companion object {
        fun fromEntity(entity: FoodInfoEntity): FoodInfoDto {
            return FoodInfoDto(
                id = entity.id,
                foodName = entity.foodName,
                foodFirstCategory = entity.foodFirstCategory,
                enerc = entity.enerc,
                prot = entity.prot,
                fatce = entity.fatce,
                chocdf = entity.chocdf
            )
        }
    }
}
