package com.rookiefit.rookiefit.workout.dto

data class WorkoutDTO(
    val workoutTitle: String,
    val workoutComment: String,
    val workoutCreatedDate: String,
    val dailyCaloriesBurned: Int,
    val workoutDetails: List<WorkoutDetailDTO>,
)
