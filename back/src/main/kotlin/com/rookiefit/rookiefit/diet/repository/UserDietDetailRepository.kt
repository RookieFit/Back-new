package com.rookiefit.rookiefit.diet.repository

import com.rookiefit.rookiefit.diet.entity.UserDietDetailEntity
import com.rookiefit.rookiefit.diet.entity.UserDietEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserDietDetailRepository : JpaRepository<UserDietDetailEntity, Long> {
    fun findByUserDietId(userDietId: Long): List<UserDietDetailEntity>

    // 특정 사용자의 특정 날짜 식단 삭제
    fun deleteAllByUserDiet(userDiet: UserDietEntity)
}
