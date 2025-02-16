package com.rookiefit.rookiefit.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.HandlerExceptionResolver
import java.util.concurrent.TimeoutException

@Component
class CustomAuthenticationEntryPoint(
    @Qualifier("handlerExceptionResolver")
    private val resolver: HandlerExceptionResolver
) : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        if (response != null && request != null) {
            val exception = when (response.status) {
                HttpServletResponse.SC_UNAUTHORIZED ->
                    authException ?: Exception("인증되지 않은 사용자입니다.")

                HttpServletResponse.SC_FORBIDDEN ->
                    Exception("접근 권한이 없습니다.")

                HttpServletResponse.SC_NOT_FOUND ->
                    ResponseStatusException(HttpStatus.NOT_FOUND, "요청하신 리소스를 찾을 수 없습니다.")

                HttpServletResponse.SC_REQUEST_TIMEOUT ->
                    TimeoutException("요청 시간이 초과되었습니다.")

                else ->
                    Exception("서버에 문제가 발생했습니다.")
            }
            resolver.resolveException(request, response, null, exception)
        }
    }
}