package com.rookiefit.rookiefit.workout.dto.response

data class WorkoutResponseDTO(
    val workoutTitle: String,
    val workoutComment: String,
    val workoutCreatedDate: String,
    val workoutImageUris: List<String>
)
