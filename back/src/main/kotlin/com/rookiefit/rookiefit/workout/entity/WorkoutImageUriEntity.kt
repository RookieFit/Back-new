package com.rookiefit.rookiefit.workout.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "workout_image")
class WorkoutImageUriEntity {
    @Id
    var imageId: Long = 0
    var imageUri: String = ""
    var imageUriCreatedDate: String = ""
}