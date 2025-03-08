package com.rookiefit.rookiefit.todo.repository

import com.rookiefit.rookiefit.todo.entity.TodoEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface TodoRepository : JpaRepository<TodoEntity, Long> {
    fun findByDate(date: LocalDate): List<TodoEntity>

    @Query("SELECT COUNT(t) FROM TodoEntity t WHERE t.completed = true")
    fun countCompletedTodos(): Long
}
