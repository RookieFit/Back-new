package com.rookiefit.rookiefit.todo.controller

import com.rookiefit.rookiefit.auth.entity.UserEntity
import com.rookiefit.rookiefit.provider.JwtProvider
import com.rookiefit.rookiefit.todo.dto.TodoRequestDTO
import com.rookiefit.rookiefit.todo.dto.TodoResponseDTO
import com.rookiefit.rookiefit.todo.entity.TodoEntity
import com.rookiefit.rookiefit.todo.service.TodoService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/todos")
class TodoController(
    private val todoService: TodoService,
    private val jwtProvider: JwtProvider
) {

    // 새로운 todo를 추가하는 API
    @PostMapping
    fun addTodo(@RequestBody todoRequest: TodoRequestDTO, @RequestHeader("Authorization") authorization: String): ResponseEntity<TodoResponseDTO> {
        val userId = jwtProvider.extractUserId(authorization.replace("Bearer ", ""))
        val user = UserEntity(userId = userId)

        val todo = TodoEntity(
            description = todoRequest.description,
            completed = todoRequest.completed,
            date = todoRequest.date,
            user = user
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
    fun deleteTodoById(@PathVariable id: Long, @RequestHeader("Authorization") authorization: String): ResponseEntity<Void> {
        val userId = jwtProvider.extractUserId(authorization.replace("Bearer ", ""))
        todoService.deleteTodoById(id, userId)
        return ResponseEntity.noContent().build()
    }

    // ID로 todo를 완료 상태로 변경하는 API
    @PutMapping("/{id}/complete")
    fun markTodoAsCompleted(@PathVariable id: Long, @RequestHeader("Authorization") authorization: String): ResponseEntity<TodoResponseDTO?> {
        val userId = jwtProvider.extractUserId(authorization.replace("Bearer ", ""))
        val updatedTodo = todoService.markTodoAsCompleted(id, userId)
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
    fun getTodosByDate(@PathVariable date: LocalDate, @RequestHeader("Authorization") authorization: String): ResponseEntity<List<TodoResponseDTO>> {
        val userId = jwtProvider.extractUserId(authorization.replace("Bearer ", ""))
        val todos = todoService.getTodosByDate(date, userId)
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
    fun countCompletedTodos(@RequestHeader("Authorization") authorization: String): ResponseEntity<Long> {
        val userId = jwtProvider.extractUserId(authorization.replace("Bearer ", ""))
        val count = todoService.countCompletedTodos(userId)
        return ResponseEntity.ok(count)
    }

    // 모든 todo를 가져오는 API
    @GetMapping("/select-all")
    fun selectAllTodos(@RequestHeader("Authorization") authorization: String): ResponseEntity<List<TodoResponseDTO>> {
        val userId = jwtProvider.extractUserId(authorization.replace("Bearer ", ""))
        val todos = todoService.selectAllTodos(userId)
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
