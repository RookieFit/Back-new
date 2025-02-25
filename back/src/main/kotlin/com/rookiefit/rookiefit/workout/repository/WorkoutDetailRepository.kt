package com.rookiefit.rookiefit.workout.repository

import com.rookiefit.rookiefit.workout.entity.WorkoutDetailEntity
import org.springframework.data.jpa.repository.JpaRepository

interface WorkoutDetailRepository: JpaRepository<WorkoutDetailEntity, Long> {
    abstract fun findByWorkoutCreatedDate(workoutCreatedDate: String): MutableList<WorkoutDetailEntity>
}