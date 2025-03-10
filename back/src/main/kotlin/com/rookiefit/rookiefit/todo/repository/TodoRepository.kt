package com.rookiefit.rookiefit.todo.repository

import com.rookiefit.rookiefit.todo.entity.TodoEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface TodoRepository : JpaRepository<TodoEntity, Long> {
    fun findByIdAndUser_UserId(id: Long, userId: String): TodoEntity?
    fun findByDateAndUser_UserId(date: LocalDate, userId: String): List<TodoEntity>
    fun countByCompletedAndUser_UserId(completed: Boolean, userId: String): Long
    fun findAllByUser_UserId(userId: String): List<TodoEntity>
}
