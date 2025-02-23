package com.rookiefit.rookiefit.workout.service

import com.rookiefit.rookiefit.auth.dto.ResponseDTO
import com.rookiefit.rookiefit.workout.dto.WorkoutDTO
import com.rookiefit.rookiefit.workout.dto.WorkoutDetailDTO
import com.rookiefit.rookiefit.workout.entity.WorkoutDetailEntity
import com.rookiefit.rookiefit.workout.entity.WorkoutEntity
import com.rookiefit.rookiefit.workout.repository.WorkoutRepository
import org.springframework.stereotype.Service

@Service
class WorkoutService(
    private val workoutRepository: WorkoutRepository
) {
    fun createWorkout(workoutDTO: WorkoutDTO): ResponseDTO {
        val workoutEntity = WorkoutEntity(
            workoutTitle = workoutDTO.workoutTitle,
            workoutComment = workoutDTO.workoutComment,
            workoutCreatedDate = workoutDTO.workoutCreatedDate,
            dailyCaloriesBurned = workoutDTO.dailyCaloriesBurned
        )
        val workoutDetailEntities = workoutDTO.workoutDetails.map {
            detail -> WorkoutDetailEntity(
                workoutName = detail.workoutName,
                reps = detail.reps,
                sets = detail.sets,
                restTime = detail.restTime,
                workoutCreatedDate = detail.workoutCreatedDate,
                workout = workoutEntity
            )
        }
        workoutEntity.workoutDetails.addAll(workoutDetailEntities)
        workoutRepository.save(workoutEntity)
        return ResponseDTO("CREATE_WORKOUT_SUCCESS", "저장되었습니다.")
    }
}