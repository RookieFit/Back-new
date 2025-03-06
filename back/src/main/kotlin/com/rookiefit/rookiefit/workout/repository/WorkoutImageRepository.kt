package com.rookiefit.rookiefit.workout.repository

import com.rookiefit.rookiefit.workout.entity.WorkoutEntity
import com.rookiefit.rookiefit.workout.entity.WorkoutImageUriEntity
import org.springframework.data.jpa.repository.JpaRepository

interface WorkoutImageRepository: JpaRepository<WorkoutImageUriEntity, Long> {
    fun findByWorkout(workout: WorkoutEntity): List<WorkoutImageUriEntity>
    fun findByWorkout_WorkoutId(workoutId: Long): List<WorkoutImageUriEntity>
    fun deleteByWorkout_WorkoutId(workoutId: Long)
}