package com.rookiefit.rookiefit.diet.controller

import com.rookiefit.rookiefit.diet.dto.FoodInfoDto
import com.rookiefit.rookiefit.diet.dto.FoodInfoRequestDto
import com.rookiefit.rookiefit.diet.service.DietService
import com.rookiefit.rookiefit.provider.JwtProvider
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/diet")
class DietController(
    private val dietService: DietService,
    private val jwtProvider: JwtProvider // JwtProvider 주입
) {

    // 식단 검색
    @GetMapping("/search")
    fun searchFood(
        @RequestParam keyword: String,
        @RequestHeader("Authorization") authorization: String
    ): List<FoodInfoDto> {
        val userId = jwtProvider.extractUserId(authorization.replace("Bearer ", ""))
        return dietService.searchFood(keyword)
    }

    // 식단 추가
    @PostMapping("/add")
    fun addFood(
        @RequestBody foodInfoRequestDto: FoodInfoRequestDto,
        @RequestHeader("Authorization") authorization: String
    ): ResponseEntity<FoodInfoDto> {
        val userId = jwtProvider.extractUserId(authorization.replace("Bearer ", ""))
        val savedFood = dietService.addFood(foodInfoRequestDto)
        return ResponseEntity.ok(savedFood)
    }
}
