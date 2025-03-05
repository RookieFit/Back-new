package com.rookiefit.rookiefit.filter

import com.rookiefit.rookiefit.auth.entity.UserEntity
import com.rookiefit.rookiefit.auth.repository.UserRepository
import com.rookiefit.rookiefit.provider.JwtProvider
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
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

    companion object {
        private val logger = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // Swagger, API Docs 등의 URL은 인증을 생략
        if (request.requestURI.startsWith("/api/auth") ||
            request.requestURI.startsWith("/v3/api-docs") ||
            request.requestURI.startsWith("/swagger-ui")) {
            filterChain.doFilter(request, response)
            return
        }

        val authorizationHeader = request.getHeader("Authorization")

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            val token = authorizationHeader.substring(7)

            try {
                val userId = jwtProvider.extractUserId(token)

                if (jwtProvider.validateToken(token)) {
                    val userEntity: UserEntity? = userRepository.findByUserId(userId)
                    if (userEntity == null) {
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
                    response.writer.write("INVALID OR EXPIRED TOKEN")
                    return
                }
            } catch (e: MalformedJwtException) {
                // Malformed JWT 처리
                logger.error("Malformed JWT exception occurred", e)
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                response.writer.write("Invalid JWT token format")
                return
            } catch (e: ExpiredJwtException) {
                // Expired JWT 처리
                logger.error("Expired JWT token exception occurred", e)
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                response.writer.write("Expired JWT token")
                return
            } catch (e: Exception) {
                // 기타 예외 처리
                logger.error("Unauthorized request exception occurred", e)
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                response.writer.write("Unauthorized request")
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
