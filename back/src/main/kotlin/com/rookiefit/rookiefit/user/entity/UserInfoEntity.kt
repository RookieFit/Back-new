package com.rookiefit.rookiefit.user.entity

import jakarta.persistence.*

@Entity
class UserInfoEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val userInfoId : Long = 0,
    var userInfoAge: Int = 0,
    var userInfoWeight: Double = 0.0,
    var userInfoHeight: Double = 0.0,
    var userInfoMuscleMass: Double = 0.0,
    var userInfoFatMass: Double = 0.0,
    var userInfoInbodyDate: String = "",
    var userInfoBasalMetabolicRate: Double = 0.0,
    @ManyToOne
    @JoinColumn(name = "user_profile_id")
    var userProfile: UserProfileEntity?,
)