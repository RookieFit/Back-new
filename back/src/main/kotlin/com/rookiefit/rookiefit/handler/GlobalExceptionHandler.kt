package com.rookiefit.rookiefit.handler

import com.rookiefit.rookiefit.auth.dto.ResponseDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.sql.SQLException

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<ResponseDTO> {
        val errorResponse = ResponseDTO("INVALID_INPUT", "잘못된 입력값입니다.")
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }
    @ExceptionHandler(SQLException::class)
    fun handleDBException(e: Exception): ResponseEntity<ResponseDTO> {
        val errorResponse = ResponseDTO("SERVER_ERROR", "서버 오류가 발생했습니다.")
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }
}