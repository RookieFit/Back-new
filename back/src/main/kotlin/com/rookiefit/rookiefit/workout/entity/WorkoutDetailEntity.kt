package com.rookiefit.rookiefit.workout.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "workout_detail")
class WorkoutDetailEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var workoutDetailId: Long = 0,
    var workoutName: String = "",
    var reps: String = "",
    var sets: String = "",
    var restTime: String = "",
    var workoutCreatedDate: String = "",
    @ManyToOne
    @JoinColumn(name = "workoutId")
    var workout: WorkoutEntity? = null
)