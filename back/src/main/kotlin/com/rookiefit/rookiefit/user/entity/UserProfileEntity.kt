package com.rookiefit.rookiefit.user.entity

import com.rookiefit.rookiefit.auth.entity.UserEntity
import com.rookiefit.rookiefit.community.entity.CommunityEntity
import com.rookiefit.rookiefit.workout.entity.WorkoutEntity
import jakarta.persistence.*

@Entity
@Table(name = "user_profile")
class UserProfileEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val userProfileId : Long = 0,
    var userProfileNickname : String = "",
    var userProfileAddress : String = "",
    var userProfileImageUri : String = "",
    var userProfileMessage: String = "",
    var userProfileGymName : String = "",
    var userProfileName: String = "",
    @OneToOne
    @JoinColumn(name = "user_id")
    var user: UserEntity,
    @OneToMany(mappedBy = "userProfile", cascade = [(CascadeType.REMOVE)])
    var userInfo : MutableList<UserInfoEntity> = mutableListOf(),
    @OneToMany(mappedBy = "userProfile", cascade = [(CascadeType.REMOVE)])
    var WorkoutEntity : MutableList<WorkoutEntity> = mutableListOf(),
    @OneToMany(mappedBy = "userProfile", cascade = [(CascadeType.REMOVE)])
    var CommunityEntity : MutableList<CommunityEntity> = mutableListOf(),
    )