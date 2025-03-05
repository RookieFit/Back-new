package com.rookiefit.rookiefit.auth.controller

import com.rookiefit.rookiefit.auth.service.PasswordResetService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class PasswordResetController(
    private val passwordResetService: PasswordResetService
) {
    // 비밀번호 변경 API
    @PostMapping("/reset-password")
    fun resetPassword(
        @RequestParam userId: String,
        @RequestParam newPassword: String
    ): ResponseEntity<String> {
        return ResponseEntity.ok(passwordResetService.resetPassword(userId, newPassword))
    }
}
