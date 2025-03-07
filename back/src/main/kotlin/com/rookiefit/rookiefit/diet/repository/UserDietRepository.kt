package com.rookiefit.rookiefit.diet.repository

import com.rookiefit.rookiefit.diet.entity.UserDietEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserDietRepository : JpaRepository<UserDietEntity, Long> {
    // user 객체를 통해 userId를 참조할 수 있도록 수정
    fun findByUser_UserIdAndDietDate(userId: String, dietDate: String): UserDietEntity?
}

