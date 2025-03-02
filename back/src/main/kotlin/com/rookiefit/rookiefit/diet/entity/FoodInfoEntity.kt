package com.rookiefit.rookiefit.diet.entity

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "food_info")
class FoodInfoEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val foodName: String,
    val foodFirstCategory: String,  // 카테고리 추가

    val enerc: Double,
    val prot: Double,
    val fatce: Double,
    val chocdf: Double
)