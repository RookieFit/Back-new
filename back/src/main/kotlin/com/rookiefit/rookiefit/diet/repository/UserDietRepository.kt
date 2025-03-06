package com.rookiefit.rookiefit.diet.repository

import com.rookiefit.rookiefit.diet.entity.UserDietEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface UserDietRepository : JpaRepository<UserDietEntity, Long> {
    // 특정 사용자와 날짜로 식단 정보를 조회
    fun findByUserIdAndDietDate(userId: String, dietDate: String): UserDietEntity?
}


