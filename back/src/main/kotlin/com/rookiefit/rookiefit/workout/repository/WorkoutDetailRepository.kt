package com.rookiefit.rookiefit.workout.repository

import com.rookiefit.rookiefit.workout.entity.WorkoutDetailEntity
import org.springframework.data.jpa.repository.JpaRepository

interface WorkoutDetailRepository: JpaRepository<WorkoutDetailEntity, Long> {
    fun findByWorkoutCreatedDate(workoutCreatedDate: String): MutableList<WorkoutDetailEntity>
    fun deleteByWorkout_WorkoutId(workoutId: Long): Int
}