package com.rookiefit.rookiefit.workout.service

import com.rookiefit.rookiefit.auth.dto.ResponseDTO
import com.rookiefit.rookiefit.common.FirebaseService
import com.rookiefit.rookiefit.workout.dto.WorkoutDTO
import com.rookiefit.rookiefit.workout.dto.response.WorkoutResponseDTO
import com.rookiefit.rookiefit.workout.entity.WorkoutDetailEntity
import com.rookiefit.rookiefit.workout.entity.WorkoutEntity
import com.rookiefit.rookiefit.workout.entity.WorkoutImageUriEntity
import com.rookiefit.rookiefit.workout.repository.WorkoutImageRepository
import com.rookiefit.rookiefit.workout.repository.WorkoutRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class WorkoutService(
    private val workoutRepository: WorkoutRepository,
    private val workoutImageRepository: WorkoutImageRepository,
    private val firebaseService: FirebaseService
) {
    @Transactional
    fun createWorkout(workoutDTO: WorkoutDTO, images: List<MultipartFile>?): ResponseDTO {
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
        val imageUris: List<String>? = images?.let { firebaseService.uploadImageFiles(it) }
        imageUris?.forEach { imageUrl ->
            val imageEntity = WorkoutImageUriEntity(imageUri = imageUrl, workout = workoutEntity)
            workoutImageRepository.save(imageEntity)
        }
        return ResponseDTO("CREATE_WORKOUT_SUCCESS", "저장되었습니다.")
    }

    //todo: 해당유저의 정보를 이미지 추가해서 넘겨줄것
    fun getWorkout(currentUserId: String?): List<WorkoutResponseDTO> {
        val workoutEntities = workoutRepository.findAll()
        return workoutEntities.map {
            entity -> WorkoutResponseDTO(
                workoutTitle = entity.workoutTitle,
                workoutComment = entity.workoutComment,
                workoutCreatedDate = entity.workoutCreatedDate
            )
        }
    }
}