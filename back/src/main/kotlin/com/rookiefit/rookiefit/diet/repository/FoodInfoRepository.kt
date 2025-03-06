package com.rookiefit.rookiefit.diet.repository

import com.rookiefit.rookiefit.diet.entity.FoodInfoEntity
import org.springframework.data.jpa.repository.JpaRepository

interface FoodInfoRepository : JpaRepository<FoodInfoEntity, Long> {

    // 음식 이름으로 음식 정보를 찾는 메서드 추가
    fun findByFoodName(foodName: String): FoodInfoEntity?

    // 음식 이름을 포함한 검색을 위한 메서드 추가 (검색 기능에 사용)
    fun findByFoodNameContaining(keyword: String): List<FoodInfoEntity>
}
