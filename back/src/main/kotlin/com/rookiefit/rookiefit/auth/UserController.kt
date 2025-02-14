package com.rookiefit.rookiefit.auth

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class UserController(private val userService: UserService) {
    @GetMapping("/id-check")
    fun idCheck(@RequestParam userId: String): Boolean {
        return userService.idCheck(userId)
    }
    @PostMapping("/sign-up")
    fun signUp(@RequestBody userDTO: UserDTO): ResponseEntity<String>{
        userService.signUp(userDTO)
        return ResponseEntity.ok("회원가입 성공")
    }
}