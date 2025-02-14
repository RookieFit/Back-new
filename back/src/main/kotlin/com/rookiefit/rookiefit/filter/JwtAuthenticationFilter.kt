package com.rookiefit.rookiefit.filter

import com.rookiefit.rookiefit.provider.JwtProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val jwtProvider: JwtProvider
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader = request.getHeader("Authorization")

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
            val token = authorizationHeader.substring(7)
            val userId = jwtProvider.extractUserId(token)

            if(userId != null && SecurityContextHolder.getContext().authentication == null) {
                if (jwtProvider.validateToken(token)) {
                    val authToken = UsernamePasswordAuthenticationToken(userId, null, emptyList())
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authToken
                } else {
                    response.status = HttpServletResponse.SC_UNAUTHORIZED
                    response.writer.write("Token is invalid or expired")
                }
            }else {
                println("User ID is null")
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                response.writer.write("Invalid token")
            }
        }
        filterChain.doFilter(request, response)
    }
}