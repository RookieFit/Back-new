package com.rookiefit.rookiefit.diet.dto

data class FoodInfoRequestDto(
    val foodName: String,
    val foodFirstCategory: String,
    val enerc: Double,
    val prot: Double,
    val fatce: Double,
    val chocdf: Double
)
