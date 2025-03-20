package com.rookiefit.rookiefit.community.controller

import com.rookiefit.rookiefit.auth.dto.ResponseDTO
import com.rookiefit.rookiefit.community.dto.request.AnswerRequestDTO
import com.rookiefit.rookiefit.community.service.AnswerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user/community/{communityId}/answer")
class AnswerController(
    private val answerService: AnswerService
) {
    @PostMapping("/create")
    fun createAnswer(
        @PathVariable communityId: Long,
        @RequestBody answerRequestDTO: AnswerRequestDTO
    ): ResponseEntity<ResponseDTO> {
        val authentication = SecurityContextHolder.getContext().authentication
        val currentUserId = authentication?.principal as? String
        val requestBody = answerService.createAnswer(currentUserId, communityId, answerRequestDTO)
        return ResponseEntity.status(HttpStatus.CREATED).body(requestBody)
    }
    @GetMapping("/list")
    fun getAnswers(
        @PathVariable communityId: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
        ): Map<String, Any>{
        return answerService.getAnswers(communityId, page, size)
    }
}