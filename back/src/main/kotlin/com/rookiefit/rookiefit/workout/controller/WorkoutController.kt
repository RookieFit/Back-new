package com.rookiefit.rookiefit.workout.controller

import com.rookiefit.rookiefit.auth.dto.ResponseDTO
import com.rookiefit.rookiefit.workout.dto.WorkoutDTO
import com.rookiefit.rookiefit.workout.dto.response.DailyCaloriesResponseDTO
import com.rookiefit.rookiefit.workout.dto.response.WorkoutDetailResponseDTO
import com.rookiefit.rookiefit.workout.dto.response.WorkoutResponseDTO
import com.rookiefit.rookiefit.workout.service.WorkoutService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/user/workout")
class WorkoutController(
    private val workoutService: WorkoutService
) {
    @PostMapping(value = ["/create"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun createWorkout(
        @RequestPart("workout") workoutDTO: WorkoutDTO,
        @RequestPart("images", required = false) images: List<MultipartFile>?,
    ) : ResponseEntity<ResponseDTO> {
        val authentication = SecurityContextHolder.getContext().authentication
        val currentUserId = authentication?.principal as? String
        val responseBody = workoutService.createWorkout(currentUserId, workoutDTO, images)
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody)
    }
    @GetMapping("/getworkout")
    fun getWorkout(): List<WorkoutResponseDTO> {
        val authentication = SecurityContextHolder.getContext().authentication
        val currentUserId = authentication?.principal as? String
        val responseBody = workoutService.getWorkout(currentUserId)
        return responseBody
    }
    @GetMapping("/getworkoutdetail")
    fun getWorkoutDetail(@RequestParam(value = "createdDate") currentDate: String): List<WorkoutDetailResponseDTO> {
        val authentication = SecurityContextHolder.getContext().authentication
        val currentUserId = authentication?.principal as? String
        val responseBody = workoutService.getWorkoutDetail(currentUserId, currentDate)
        return responseBody
    }
    @PutMapping("/update")
    fun updateWorkout(
        @RequestPart("workout") workoutDTO: WorkoutDTO,
        @RequestPart("images", required = false) images: List<MultipartFile>?,
    ): ResponseEntity<ResponseDTO> {
        val authentication = SecurityContextHolder.getContext().authentication
        val currentUserId = authentication?.principal as? String
        val responseBody = workoutService.updateWorkout(currentUserId, workoutDTO, images)
        return ResponseEntity.status(HttpStatus.OK).body(responseBody)
    }
    @GetMapping("/getdailycalorie")
    fun getDailyCalorie(): List<DailyCaloriesResponseDTO> {
        val authentication = SecurityContextHolder.getContext().authentication
        val currentUserId = authentication?.principal as String
        val responseBody = workoutService.getDailyCalorie(currentUserId)
        return responseBody
    }
}