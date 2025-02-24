package com.rookiefit.rookiefit.workout.entity

import jakarta.persistence.*

@Entity
@Table(name = "workout_image")
class WorkoutImageUriEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var imageId: Long = 0,
    var imageUri: String = "",
    var imageUriCreatedDate: String = "",
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id")
    var workout: WorkoutEntity? = null,
)