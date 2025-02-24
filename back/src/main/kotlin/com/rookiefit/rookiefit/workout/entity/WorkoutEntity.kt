package com.rookiefit.rookiefit.workout.entity

import jakarta.persistence.*

@Entity
@Table(name = "workout")
class WorkoutEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var workoutId: Long = 0,
    var workoutCreatedDate: String = "",
    var workoutTitle: String = "",
    var workoutComment: String = "",
    var dailyCaloriesBurned: Int = 0,
    @OneToMany(mappedBy = "workout", cascade = [(CascadeType.ALL)], orphanRemoval = true)
    var workoutDetails: MutableList<WorkoutDetailEntity> = mutableListOf(),
    @OneToMany(mappedBy = "workout", cascade = [CascadeType.ALL], orphanRemoval = true)
    var images: MutableList<WorkoutImageUriEntity> = mutableListOf()
    )