package com.rookiefit.rookiefit.auth.entity

import com.rookiefit.rookiefit.diet.entity.UserDietEntity
import com.rookiefit.rookiefit.user.entity.UserProfileEntity
import jakarta.persistence.*

@Entity
@Table(name = "user_auth")
class UserEntity (
    @Id
    @Column(name = "user_id")
    val userId: String = "",

    @Column(name = "user_password")
    var userPassword: String = "",

    @Column(name = "user_phone_number")
    var userPhoneNumber: String = "",

    @Column(name = "role")
    var role: String = "",

    @Column(name = "type")
    var type: String = "",

    @Column(name = "is_deleted")
    var isDeleted: Boolean = false,

    @Column(name = "subscribed_date")
    var subscribedDate: String = "",

    @Column(name = "is_licensed")
    var isLicensed: Boolean = false,

    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var userProfile: UserProfileEntity? = null,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var userDiets: MutableList<UserDietEntity> = mutableListOf()
)

