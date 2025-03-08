package com.rookiefit.rookiefit.todo.controller

import com.rookiefit.rookiefit.todo.dto.TodoRequestDTO
import com.rookiefit.rookiefit.todo.dto.TodoResponseDTO
import com.rookiefit.rookiefit.todo.entity.TodoEntity
import com.rookiefit.rookiefit.todo.service.TodoService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/todos")
class TodoController(private val todoService: TodoService) {

    // 새로운 todo를 추가하는 API
    @PostMapping
    fun addTodo(@RequestBody todoRequest: TodoRequestDTO): ResponseEntity<TodoResponseDTO> {
        val todo = TodoEntity(
            description = todoRequest.description,
            completed = todoRequest.completed,
            date = todoRequest.date
        )
        val createdTodo = todoService.addTodo(todo)
        val response = TodoResponseDTO(
            id = createdTodo.id,
            description = createdTodo.description,
            completed = createdTodo.completed,
            date = createdTodo.date
        )
        return ResponseEntity.ok(response)
    }

    // ID로 todo를 삭제하는 API
    @DeleteMapping("/{id}")
    fun deleteTodoById(@PathVariable id: Long): ResponseEntity<Void> {
        todoService.deleteTodoById(id)
        return ResponseEntity.noContent().build()
    }

    // ID로 todo를 완료 상태로 변경하는 API
    @PutMapping("/{id}/complete")
    fun markTodoAsCompleted(@PathVariable id: Long): ResponseEntity<TodoResponseDTO?> {
        val updatedTodo = todoService.markTodoAsCompleted(id)
        return if (updatedTodo != null) {
            val response = TodoResponseDTO(
                id = updatedTodo.id,
                description = updatedTodo.description,
                completed = updatedTodo.completed,
                date = updatedTodo.date
            )
            ResponseEntity.ok(response)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    // 특정 날짜의 todo를 가져오는 API
    @GetMapping("/date/{date}")
    fun getTodosByDate(@PathVariable date: LocalDate): ResponseEntity<List<TodoResponseDTO>> {
        val todos = todoService.getTodosByDate(date)
        val response = todos.map { todo ->
            TodoResponseDTO(
                id = todo.id,
                description = todo.description,
                completed = todo.completed,
                date = todo.date
            )
        }
        return ResponseEntity.ok(response)
    }

    // 완료된 todo의 개수를 세는 API
    @GetMapping("/completed/count")
    fun countCompletedTodos(): ResponseEntity<Long> {
        val count = todoService.countCompletedTodos()
        return ResponseEntity.ok(count)
    }

    // 모든 todo를 가져오는 API
    @GetMapping("/select-all")
    fun selectAllTodos(): ResponseEntity<List<TodoResponseDTO>> {
        val todos = todoService.selectAllTodos()
        val response = todos.map { todo ->
            TodoResponseDTO(
                id = todo.id,
                description = todo.description,
                completed = todo.completed,
                date = todo.date
            )
        }
        return ResponseEntity.ok(response)
    }
}
