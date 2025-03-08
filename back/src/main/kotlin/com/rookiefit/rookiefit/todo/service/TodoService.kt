package com.rookiefit.rookiefit.todo.service

import com.rookiefit.rookiefit.todo.entity.TodoEntity
import com.rookiefit.rookiefit.todo.repository.TodoRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class TodoService(private val todoRepository: TodoRepository) {

    fun addTodo(todo: TodoEntity): TodoEntity {
        return todoRepository.save(todo)
    }

    fun deleteTodoById(id: Long, userId: String) {
        val todo = todoRepository.findByIdAndUser_UserId(id, userId)
        todo?.let { todoRepository.delete(it) }
    }

    fun markTodoAsCompleted(id: Long, userId: String): TodoEntity? {
        val todo = todoRepository.findByIdAndUser_UserId(id, userId)
        todo?.let {
            it.completed = true
            return todoRepository.save(it)
        }
        return null
    }

    fun getTodosByDate(date: LocalDate, userId: String): List<TodoEntity> {
        return todoRepository.findByDateAndUser_UserId(date, userId)
    }

    fun countCompletedTodos(userId: String): Long {
        return todoRepository.countByCompletedAndUser_UserId(true, userId)
    }

    fun selectAllTodos(userId: String): List<TodoEntity> {
        return todoRepository.findAllByUser_UserId(userId)
    }
}
