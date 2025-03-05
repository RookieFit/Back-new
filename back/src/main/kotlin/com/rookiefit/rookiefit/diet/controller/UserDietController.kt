package com.rookiefit.rookiefit.diet.controller

import com.rookiefit.rookiefit.diet.dto.UserDietDto
import com.rookiefit.rookiefit.diet.dto.request.AddFoodToDietRequest
import com.rookiefit.rookiefit.diet.service.UserDietService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/diet")
class UserDietController(private val userDietService: UserDietService) {

    // 특정 날짜의 식단 조회
    @GetMapping
    fun getUserDiet(
        @RequestParam userId: String,
        @RequestParam dietDate: String
    ): ResponseEntity<UserDietDto?> {
        val diet = userDietService.getUserDietList(userId, dietDate)
        return ResponseEntity.ok(diet)
    }

    // 식단에 음식 추가
    @PostMapping("/list/add")
    fun addFoodToDiet(@RequestBody request: AddFoodToDietRequest): ResponseEntity<UserDietDto> {
        val updatedDiet = userDietService.addFoodToDiet(request)
        return ResponseEntity.ok(updatedDiet)
    }

    // 식단에서 특정 음식 삭제
    @DeleteMapping("/list/delete/{detailId}")
    fun deleteFoodFromDiet(@PathVariable detailId: Long): ResponseEntity<Unit> {
        userDietService.deleteFoodFromDiet(detailId)
        return ResponseEntity.noContent().build()
    }

    // 특정 날짜의 식단 삭제
    @DeleteMapping("/delete")
    fun deleteDietByDate(
        @RequestParam userId: String,
        @RequestParam dietDate: String
    ): ResponseEntity<String> {
        return userDietService.deleteDietByDate(userId, dietDate)
    }

}
