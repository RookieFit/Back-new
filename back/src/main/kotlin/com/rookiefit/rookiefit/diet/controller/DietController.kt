package com.rookiefit.rookiefit.diet.controller

import com.rookiefit.rookiefit.diet.dto.FoodInfoDto
import com.rookiefit.rookiefit.diet.service.DietService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/diet")
class DietController(private val dietService: DietService) {

    // 식단 검색
    @GetMapping("/search")
    fun searchFood(@RequestParam keyword: String): List<FoodInfoDto> {
        return dietService.searchFood(keyword)
    }
}
