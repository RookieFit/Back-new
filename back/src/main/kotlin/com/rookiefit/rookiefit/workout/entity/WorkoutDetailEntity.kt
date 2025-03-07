package com.rookiefit.rookiefit.workout.entity

import jakarta.persistence.*

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id")
    var workout: WorkoutEntity? = null
)