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

    fun deleteTodoById(id: Long) {
        todoRepository.deleteById(id)
    }

    fun markTodoAsCompleted(id: Long): TodoEntity? {
        val todo = todoRepository.findById(id).orElse(null)
        todo?.let {
            it.completed = true
            return todoRepository.save(it)
        }
        return null
    }

    fun getTodosByDate(date: LocalDate): List<TodoEntity> {
        return todoRepository.findByDate(date)
    }

    fun countCompletedTodos(): Long {
        return todoRepository.countCompletedTodos()
    }

    fun selectAllTodos(): List<TodoEntity> {
        return todoRepository.findAll()
    }
}
