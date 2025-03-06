package com.rookiefit.rookiefit.diet.service

import com.rookiefit.rookiefit.diet.dto.*
import com.rookiefit.rookiefit.diet.entity.*
import com.rookiefit.rookiefit.diet.repository.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DietService(
    @Autowired private val foodInfoRepository: FoodInfoRepository,
    @Autowired private val userDietRepository: UserDietRepository,
    @Autowired private val userDietDetailRepository: UserDietDetailRepository
) {

    // 음식 검색
    fun searchFood(keyword: String): List<FoodInfoDto> {
        val trimmedKeyword = keyword.trim()
        val foods = foodInfoRepository.findByFoodNameContaining(trimmedKeyword)
        return foods.map { FoodInfoDto.fromEntity(it) }
    }

    // food_info에 없는 음식 추가
    fun addFood(foodInfoRequestDto: FoodInfoRequestDto): FoodInfoDto {
        // 1. 같은 foodName이 있는지 확인
        val existingFood = foodInfoRepository.findByFoodName(foodInfoRequestDto.foodName)
        if (existingFood != null) {
            throw IllegalArgumentException("이미 존재하는 음식입니다.")  // 중복이면 예외 발생
        }

        // 2. 새로운 음식 저장
        val foodEntity = FoodInfoEntity(
            foodName = foodInfoRequestDto.foodName,
            foodFirstCategory = foodInfoRequestDto.foodFirstCategory,
            enerc = foodInfoRequestDto.enerc,
            prot = foodInfoRequestDto.prot,
            fatce = foodInfoRequestDto.fatce,
            chocdf = foodInfoRequestDto.chocdf
        )

        val savedFood = foodInfoRepository.save(foodEntity)
        return FoodInfoDto.fromEntity(savedFood)
    }
}
