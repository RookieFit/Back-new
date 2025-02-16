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
        if (request.requestURI.startsWith("/api/auth")) {
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
                        response.writer.write("사용자를 찾을수 없습니다")
                        return
                    }
                    val authorities = listOf(SimpleGrantedAuthority(userEntity.role))
                    val authToken = UsernamePasswordAuthenticationToken(userId, null, authorities)
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authToken
                } else {
                    response.status = HttpServletResponse.SC_UNAUTHORIZED
                    response.writer.write("유효하지않은 토큰")
                    return
                }
            } else {
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                response.writer.write("유효하지않은 토큰")
                return
            }
        } else {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.writer.write("누락된 토큰")
            return
        }
        filterChain.doFilter(request, response)
    }
}