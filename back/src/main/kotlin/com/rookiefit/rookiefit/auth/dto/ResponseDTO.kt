package com.rookiefit.rookiefit.auth.dto

open class ResponseDTO(
    val code: String = "SUCCESS",
    val message: String = "Request was successfull.."
) {
    companion object {
        fun createResponse(code: String, message: String): ResponseDTO {
            return ResponseDTO(code, message)
        }
    }
}