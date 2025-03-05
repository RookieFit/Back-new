package com.rookiefit.rookiefit.diet.service

import com.rookiefit.rookiefit.diet.dto.UserDietDto
import com.rookiefit.rookiefit.diet.dto.request.AddFoodToDietRequest
import com.rookiefit.rookiefit.diet.entity.UserDietDetailEntity
import com.rookiefit.rookiefit.diet.entity.UserDietEntity
import com.rookiefit.rookiefit.diet.repository.FoodInfoRepository
import com.rookiefit.rookiefit.diet.repository.UserDietDetailRepository
import com.rookiefit.rookiefit.diet.repository.UserDietRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserDietService(
    private val userDietRepository: UserDietRepository,
    private val userDietDetailRepository: UserDietDetailRepository,
    private val foodInfoRepository: FoodInfoRepository
) {

    // 특정 날짜의 식단 조회
    fun getUserDietList(userId: String, dietDate: String): UserDietDto? {
        val userDiet = userDietRepository.findByUserIdAndDietDate(userId, dietDate) ?: return null
        return UserDietDto.fromEntity(userDiet)
    }

    // 식단에 음식 추가
    @Transactional
    fun addFoodToDiet(request: AddFoodToDietRequest): UserDietDto {
        val userDiet = userDietRepository.findByUserIdAndDietDate(request.userId, request.dietDate)
            ?: UserDietEntity(userId = request.userId, dietDate = request.dietDate).also {
                userDietRepository.save(it)  // 없으면 새로 생성 후 저장
            }

        // 음식 추가
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

        // 기존 데이터 삭제 후 새 데이터 추가 (덮어쓰기 개념)
        userDietDetailRepository.deleteAllByUserDiet(userDiet)
        userDiet.details.clear()
        userDiet.details.addAll(foodDetails)

        userDiet.updateTotalCalories()

        val savedDiet = userDietRepository.save(userDiet)
        return UserDietDto.fromEntity(savedDiet)
    }

    // 식단에서 특정 음식 삭제
    @Transactional
    fun deleteFoodFromDiet(userDietDetailId: Long): ResponseEntity<String> {
        val userDietDetail = userDietDetailRepository.findById(userDietDetailId).orElse(null)
            ?: throw IllegalArgumentException("해당 음식이 식단에 존재하지 않습니다.")

        val userDiet = userDietDetail.userDiet

        // 음식 삭제
        userDietDetailRepository.deleteById(userDietDetailId)

        // JPA가 삭제를 반영하도록 flush 호출
        userDietDetailRepository.flush()

        // 삭제 후 totalCalories를 갱신
        userDiet.updateTotalCalories()

        // 삭제 후 해당 음식이 존재하지 않는지 확인
        val deletedFood = userDietDetailRepository.findById(userDietDetailId).orElse(null)
        return if (deletedFood == null) {
            ResponseEntity.ok("음식이 성공적으로 삭제되었습니다.")
        } else {
            ResponseEntity.status(500).body("음식 삭제에 실패했습니다.")
        }
    }
}
