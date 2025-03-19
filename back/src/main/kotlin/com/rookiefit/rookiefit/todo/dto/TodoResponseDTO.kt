package com.rookiefit.rookiefit.todo.dto

import java.time.LocalDate

data class TodoResponseDTO(
    val id: Long,
    val description: String,
    val completed: Boolean,
    val date: LocalDate
)
