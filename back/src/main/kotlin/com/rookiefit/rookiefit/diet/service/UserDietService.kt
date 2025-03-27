package com.rookiefit.rookiefit.diet.service

import com.rookiefit.rookiefit.diet.dto.UserDietDto
import com.rookiefit.rookiefit.diet.dto.request.AddFoodToDietRequest
import com.rookiefit.rookiefit.diet.entity.UserDietDetailEntity
import com.rookiefit.rookiefit.diet.entity.UserDietEntity
import com.rookiefit.rookiefit.diet.repository.FoodInfoRepository
import com.rookiefit.rookiefit.diet.repository.UserDietDetailRepository
import com.rookiefit.rookiefit.diet.repository.UserDietRepository
import com.rookiefit.rookiefit.auth.repository.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserDietService(
    private val userDietRepository: UserDietRepository,
    private val userDietDetailRepository: UserDietDetailRepository,
    private val foodInfoRepository: FoodInfoRepository,
    private val userRepository: UserRepository
) {

    // 특정 날짜의 식단 조회
    fun getUserDietList(userId: String, dietDate: String): UserDietDto? {
        val userDiet = userDietRepository.findByUser_UserIdAndDietDate(userId, dietDate) ?: return null
        return UserDietDto.fromEntity(userDiet)
    }

    // 식단에 음식 추가
    @Transactional
    fun addFoodToDiet(userId: String, request: AddFoodToDietRequest): UserDietDto {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found: $userId") }

        val userDiet = userDietRepository.findByUser_UserIdAndDietDate(userId, request.dietDate)
            ?: UserDietEntity(user = user, dietDate = request.dietDate).also {
                userDietRepository.save(it)
            }

        // 기존 데이터 삭제 (DB에서도 삭제)
        userDietDetailRepository.deleteAll(userDiet.details)
        userDiet.details.clear()

        // 새 음식 추가
        val foodDetails = request.dietDetails.map { detail ->
            val foodInfo = foodInfoRepository.findByFoodName(detail.foodName)
                ?: throw IllegalArgumentException("${detail.foodName} 식품이 존재하지 않습니다.")

            UserDietDetailEntity(
                userDiet = userDiet,
                foodName = foodInfo.foodName,
                foodFirstCategory = foodInfo.foodFirstCategory,
                chocdf = foodInfo.chocdf,
                prot = foodInfo.prot,
                fatce = foodInfo.fatce,
                enerc = foodInfo.enerc,
                dietDate = userDiet.dietDate
            )
        }

        userDiet.details.addAll(foodDetails)
        userDiet.updateTotalCalories()

        return UserDietDto.fromEntity(userDietRepository.save(userDiet))
    }

    // 식단에서 특정 음식 삭제
    /*@Transactional
    fun deleteFoodFromDiet(userId: String, userDietDetailId: Long): ResponseEntity<String> {
        val userDietDetail = userDietDetailRepository.findById(userDietDetailId)
            .orElseThrow { IllegalArgumentException("해당 음식이 식단에 존재하지 않습니다.") }

        val userDiet = userDietDetail.userDiet

        // 삭제된 userDietDetail을 userDiet의 details 리스트에서 제거
        userDiet.details.remove(userDietDetail)

        // 음식 삭제
        userDietDetailRepository.deleteById(userDietDetailId)

        // 삭제 후 totalCalories를 갱신
        userDiet.updateTotalCalories()

        // 변경된 정보를 저장 (삭제 후 갱신된 총 칼로리 반영)
        userDietRepository.save(userDiet)

        return ResponseEntity.ok("음식이 성공적으로 삭제되었습니다.")
    }*/

    // 특정 날짜의 식단 삭제
    @Transactional
    fun deleteDietByDate(userId: String, dietDate: String): ResponseEntity<String> {
        val userDiet = userDietRepository.findByUser_UserIdAndDietDate(userId, dietDate)
            ?: throw IllegalArgumentException("해당 날짜의 식단이 존재하지 않습니다.")

        // 식단 삭제
        userDietRepository.delete(userDiet)

        return ResponseEntity.ok("해당 날짜의 식단이 성공적으로 삭제되었습니다.")
    }
}
