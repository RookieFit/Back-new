package com.rookiefit.rookiefit.todo.dto

import java.time.LocalDate

data class TodoRequestDTO(
    val description: String,
    val completed: Boolean,
    val date: LocalDate,
    val userId: Long
)
