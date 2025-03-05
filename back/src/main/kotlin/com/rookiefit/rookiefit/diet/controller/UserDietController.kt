package com.rookiefit.rookiefit.diet.controller

import com.rookiefit.rookiefit.diet.dto.UserDietDto
import com.rookiefit.rookiefit.diet.dto.request.AddFoodToDietRequest
import com.rookiefit.rookiefit.diet.service.UserDietService
import com.rookiefit.rookiefit.provider.JwtProvider
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/diet")
class UserDietController(private val userDietService: UserDietService, private val jwtProvider: JwtProvider) {

    // 특정 날짜의 식단 조회
    @GetMapping
    fun getUserDiet(
        @RequestHeader("Authorization") authorization: String,
        @RequestParam dietDate: String
    ): ResponseEntity<UserDietDto?> {
        val userId = jwtProvider.extractUserId(authorization.replace("Bearer ", ""))
        val diet = userDietService.getUserDietList(userId, dietDate)
        return ResponseEntity.ok(diet)
    }

    // 식단에 음식 추가
    @PostMapping("/list/add")
    fun addFoodToDiet(@RequestBody request: AddFoodToDietRequest, @RequestHeader("Authorization") authorization: String): ResponseEntity<UserDietDto> {
        val userId = jwtProvider.extractUserId(authorization.replace("Bearer ", ""))
        val updatedDiet = userDietService.addFoodToDiet(request)
        return ResponseEntity.ok(updatedDiet)
    }

    // 식단에서 특정 음식 삭제
    @DeleteMapping("/list/delete/{detailId}")
    fun deleteFoodFromDiet(@PathVariable detailId: Long, @RequestHeader("Authorization") authorization: String): ResponseEntity<Unit> {
        val userId = jwtProvider.extractUserId(authorization.replace("Bearer ", ""))
        userDietService.deleteFoodFromDiet(detailId)
        return ResponseEntity.noContent().build()
    }

    // 특정 날짜의 식단 삭제
    @DeleteMapping("/delete")
    fun deleteDietByDate(
        @RequestHeader("Authorization") authorization: String,
        @RequestParam dietDate: String
    ): ResponseEntity<String> {
        val userId = jwtProvider.extractUserId(authorization.replace("Bearer ", ""))
        return userDietService.deleteDietByDate(userId, dietDate)
    }
}


