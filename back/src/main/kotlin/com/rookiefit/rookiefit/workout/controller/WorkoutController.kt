package com.rookiefit.rookiefit.workout.controller

import com.rookiefit.rookiefit.auth.dto.ResponseDTO
import com.rookiefit.rookiefit.workout.dto.WorkoutDTO
import com.rookiefit.rookiefit.workout.service.WorkoutService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/user/workout")
class WorkoutController(private val workoutService: WorkoutService) {
    @PostMapping(value = ["/create"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun createWorkout(
        @RequestPart("workout") workoutDTO: WorkoutDTO,
        @RequestPart("images", required = false) images: List<MultipartFile>?,
    ) : ResponseEntity<ResponseDTO> {
        val responseBody = workoutService.createWorkout(workoutDTO)
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody)
    }
}