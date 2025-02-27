package com.rookiefit.rookiefit.user.entity

import com.rookiefit.rookiefit.auth.UserEntity
import com.rookiefit.rookiefit.workout.entity.WorkoutEntity
import jakarta.persistence.*

@Entity
class UserProfileEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val userProfileId : Long = 0,
    var userProfileNickname : String = "",
    var userProfileAddress : String = "",
    var userProfileImageUri : String = "",
    var userProfileMessage: String = "",
    var userProfileGymName : String = "",
    @OneToOne
    @JoinColumn(name = "user_id")
    var user: UserEntity,
    @OneToMany(mappedBy = "userProfile", cascade = [(CascadeType.REMOVE)])
    var userInfo : MutableList<UserInfoEntity> = mutableListOf(),
    @OneToMany(mappedBy = "userProfile", cascade = [(CascadeType.REMOVE)])
    var WorkoutEntity : MutableList<WorkoutEntity> = mutableListOf(),

    )