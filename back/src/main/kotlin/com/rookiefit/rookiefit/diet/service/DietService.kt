package com.rookiefit.rookiefit.diet.service

import com.rookiefit.rookiefit.diet.dto.FoodInfoDto
import com.rookiefit.rookiefit.diet.entity.FoodInfoEntity
import com.rookiefit.rookiefit.diet.repository.FoodInfoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DietService(
    @Autowired private val foodInfoRepository: FoodInfoRepository
) {
    fun searchFood(keyword: String): List<FoodInfoDto> {
        val trimmedKeyword = keyword.trim()
        val foods = foodInfoRepository.findByFoodNameContaining(trimmedKeyword)
        return foods.map { FoodInfoDto.fromEntity(it) }
    }
}
