package com.rookiefit.rookiefit.filter

import com.rookiefit.rookiefit.auth.UserEntity
import com.rookiefit.rookiefit.auth.UserRepository
import com.rookiefit.rookiefit.provider.JwtProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Order(0)
@Component
class JwtAuthenticationFilter(
    private val jwtProvider: JwtProvider,
    private val userRepository: UserRepository
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (
            request.requestURI.startsWith("/api/auth") ||
            request.requestURI.startsWith("/v3/api-docs") ||
            request.requestURI.startsWith("/swagger-ui")
            ) {
            with(filterChain) { doFilter(request, response) }
            return
        }
        val authorizationHeader = request.getHeader("Authorization")

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            val token = authorizationHeader.substring(7)
            val userId = jwtProvider.extractUserId(token)

            if (userId != null) {
                if (jwtProvider.validateToken(token)) {
                    val userEntity: UserEntity? = userRepository.findByUserId(userId)
                    if(userEntity == null){
                        response.status = HttpServletResponse.SC_NOT_FOUND
                        response.writer.write("NOT FOUND USER")
                        return
                    }
                    val authorities = listOf(SimpleGrantedAuthority(userEntity.role))
                    val authToken = UsernamePasswordAuthenticationToken(userId, null, authorities)
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authToken
                } else {
                    response.status = HttpServletResponse.SC_UNAUTHORIZED
                    response.writer.write("INVAILD OR EXPIRED TOKEN")
                    return
                }
            } else {
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                response.writer.write("INVAILD TOKEN")
                return
            }
        } else {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.writer.write("TOKEN IS MISSING")
            return
        }
        filterChain.doFilter(request, response)
    }
}