package com.rookiefit.rookiefit.workout.repository

import com.rookiefit.rookiefit.workout.entity.WorkoutEntity
import org.springframework.data.jpa.repository.JpaRepository

interface WorkoutRepository: JpaRepository<WorkoutEntity, Long> {
}