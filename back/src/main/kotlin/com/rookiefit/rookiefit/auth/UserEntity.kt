package com.rookiefit.rookiefit.auth

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "user_auth")
class UserEntity (
    @Id
    @Column(name = "user_id")
    var userId: String = "",

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
)