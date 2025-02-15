package com.rookiefit.rookiefit.testcontroller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {
    @GetMapping("/api/user/protected")
    fun protectedEndPoint(): String {
        return "접근 성공"
    }
}