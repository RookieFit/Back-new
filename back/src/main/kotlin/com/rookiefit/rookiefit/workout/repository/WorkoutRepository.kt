package com.rookiefit.rookiefit.workout.repository

import com.rookiefit.rookiefit.workout.entity.WorkoutEntity
import org.springframework.data.jpa.repository.JpaRepository

interface WorkoutRepository: JpaRepository<WorkoutEntity, Long> {
    fun findByUserProfile_UserProfileIdAndWorkoutCreatedDate(
        userProfileId: Long,
        workoutCreatedDate: String
    ) : WorkoutEntity
    fun findByUserProfile_UserProfileId(userProfileId: Long): List<WorkoutEntity>
    fun findTop7ByUserProfile_UserProfileIdOrderByWorkoutCreatedDate(userProfileId: Long): List<WorkoutEntity>
}