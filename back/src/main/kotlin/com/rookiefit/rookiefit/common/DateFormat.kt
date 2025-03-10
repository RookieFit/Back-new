package com.rookiefit.rookiefit.common

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateFormat {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    fun now(): String {
        return LocalDateTime.now().format(formatter)
    }
}