package com.rookiefit.rookiefit.todo.entity

import com.rookiefit.rookiefit.auth.entity.UserEntity
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "todos")
class TodoEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "description")
    var description: String = "",

    @Column(name = "completed")
    var completed: Boolean = false,

    @Column(name = "date")
    var date: LocalDate = LocalDate.now(),

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    var user: UserEntity
)
