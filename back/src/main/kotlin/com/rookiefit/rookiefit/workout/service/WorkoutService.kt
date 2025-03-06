package com.rookiefit.rookiefit.workout.service

import com.rookiefit.rookiefit.auth.dto.ResponseDTO
import com.rookiefit.rookiefit.common.FirebaseService
import com.rookiefit.rookiefit.user.repository.UserProfileRepository
import com.rookiefit.rookiefit.workout.dto.WorkoutDTO
import com.rookiefit.rookiefit.workout.dto.response.WorkoutDetailResponseDTO
import com.rookiefit.rookiefit.workout.dto.response.WorkoutResponseDTO
import com.rookiefit.rookiefit.workout.entity.WorkoutDetailEntity
import com.rookiefit.rookiefit.workout.entity.WorkoutEntity
import com.rookiefit.rookiefit.workout.entity.WorkoutImageUriEntity
import com.rookiefit.rookiefit.workout.repository.WorkoutDetailRepository
import com.rookiefit.rookiefit.workout.repository.WorkoutImageRepository
import com.rookiefit.rookiefit.workout.repository.WorkoutRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException

@Service
class WorkoutService(
    private val userProfileRepository: UserProfileRepository,
    private val workoutRepository: WorkoutRepository,
    private val workoutImageRepository: WorkoutImageRepository,
    private val workoutDetailRepository: WorkoutDetailRepository,
    private val firebaseService: FirebaseService
) {
    @Transactional
    fun createWorkout(currentUserId: String?, workoutDTO: WorkoutDTO, images: List<MultipartFile>?): ResponseDTO {
        val userProfileEntity = userProfileRepository.findByUser_UserId(currentUserId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 유저")
        val workoutEntity = WorkoutEntity(
            workoutTitle = workoutDTO.workoutTitle,
            workoutComment = workoutDTO.workoutComment,
            workoutCreatedDate = workoutDTO.workoutCreatedDate,
            dailyCaloriesBurned = workoutDTO.dailyCaloriesBurned,
            userProfile = userProfileEntity
        )
        val workoutDetailEntities = workoutDTO.workoutDetails.map {
            detail -> WorkoutDetailEntity(
                workoutName = detail.workoutName,
                reps = detail.reps,
                sets = detail.sets,
                restTime = detail.restTime,
                workoutCreatedDate = workoutDTO.workoutCreatedDate,
                workout = workoutEntity
            )
        }
        workoutEntity.workoutDetails.addAll(workoutDetailEntities)
        workoutRepository.save(workoutEntity)
        val imageUris: List<String>? = images?.let { firebaseService.uploadImageFiles(it) }
        imageUris?.forEach { imageUrl ->
            val imageEntity = WorkoutImageUriEntity(
                workoutImageUri = imageUrl, 
                workoutImageUriCreatedDate = workoutDTO.workoutCreatedDate,
                workout = workoutEntity
            )
            workoutImageRepository.save(imageEntity)
        }
        return ResponseDTO("CREATE_WORKOUT_SUCCESS", "저장되었습니다.")
    }

    fun getWorkout(currentUserId: String?): List<WorkoutResponseDTO> {
        val userProfileEntity = userProfileRepository.findByUser_UserId(currentUserId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지않는 유저")
        val workoutEntities = workoutRepository.findByUserProfile_UserProfileId(
            userProfileEntity.userProfileId
        )
        return workoutEntities.map {
            entity -> WorkoutResponseDTO(
                workoutTitle = entity.workoutTitle,
                workoutComment = entity.workoutComment,
                workoutCreatedDate = entity.workoutCreatedDate,
                workoutImageUris = workoutImageRepository.findByWorkout(entity).map { it.workoutImageUri }
            )
        }
    }

    fun getWorkoutDetail(currentUserId: String?, currentDate: String): List<WorkoutDetailResponseDTO> {
        val userProfileEntity = userProfileRepository.findByUser_UserId(currentUserId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지않는 유저")
        val userWorkoutEntity = workoutRepository.findByUserProfile_UserProfileIdAndWorkoutCreatedDate(
            userProfileEntity.userProfileId, currentDate
        )
        val workoutDetailEntities = workoutDetailRepository.findByWorkout_WorkoutIdAndWorkoutCreatedDate(userWorkoutEntity.workoutId,currentDate)
        return workoutDetailEntities.map {
            entity -> WorkoutDetailResponseDTO(
                workoutName = entity.workoutName,
                reps = entity.reps,
                sets = entity.sets,
                restTime = entity.restTime,
            )
        }
    }

    @Transactional
    fun updateWorkout(
        currentUserId: String?,
        workoutDTO: WorkoutDTO,
        images: List<MultipartFile>? // 새로운 이미지 리스트
    ): ResponseDTO {
        val userProfileEntity = userProfileRepository.findByUser_UserId(currentUserId)
            ?: return ResponseDTO("USER_NOT_FOUND", "유저를 찾을 수 없습니다.")

        val existingWorkoutEntity = workoutRepository.findByUserProfile_UserProfileIdAndWorkoutCreatedDate(
            userProfileEntity.userProfileId,
            workoutDTO.workoutCreatedDate
        ) ?: return ResponseDTO("WORKOUT_NOT_FOUND", "해당 날짜의 운동 기록이 없습니다.")

        // 운동 정보 업데이트
        existingWorkoutEntity.workoutTitle = workoutDTO.workoutTitle
        existingWorkoutEntity.workoutComment = workoutDTO.workoutComment
        existingWorkoutEntity.dailyCaloriesBurned = workoutDTO.dailyCaloriesBurned

        // 기존 운동 상세 정보 삭제 후 새로운 데이터 추가
        workoutDetailRepository.deleteByWorkout_WorkoutId(existingWorkoutEntity.workoutId)

        val newWorkoutDetails = workoutDTO.workoutDetails.map { newDetail ->
            WorkoutDetailEntity(
                workout = existingWorkoutEntity,
                workoutName = newDetail.workoutName,
                reps = newDetail.reps,
                sets = newDetail.sets,
                restTime = newDetail.restTime,
                workoutCreatedDate = workoutDTO.workoutCreatedDate
            )
        }

        try {
            workoutDetailRepository.saveAll(newWorkoutDetails)
        } catch (e: Exception) {
            println("Error saving workout details: ${e.message}")
            throw e
        }

        if (!images.isNullOrEmpty()) {
            val existingImages = workoutImageRepository.findByWorkout_WorkoutId(existingWorkoutEntity.workoutId)
            existingImages.forEach { imageEntity ->
                firebaseService.deleteImageFile(imageEntity.workoutImageUri)
            }
            workoutImageRepository.deleteByWorkout_WorkoutId(existingWorkoutEntity.workoutId)
            val imageUris = firebaseService.uploadImageFiles(images)
            val newImageEntities = imageUris.map { imageUrl ->
                WorkoutImageUriEntity(
                    workoutImageUri = imageUrl,
                    workoutImageUriCreatedDate = workoutDTO.workoutCreatedDate,
                    workout = existingWorkoutEntity
                )
            }
            workoutImageRepository.saveAll(newImageEntities) // 새로운 이미지 저장
        }

        // 운동 기록 저장
        workoutRepository.save(existingWorkoutEntity)
        return ResponseDTO("UPDATE_SUCCESS", "운동 기록이 성공적으로 업데이트되었습니다.")
    }

}