package com.rookiefit.rookiefit.config

import com.rookiefit.rookiefit.auth.UserRepository
import com.rookiefit.rookiefit.filter.JwtAuthenticationFilter
import com.rookiefit.rookiefit.handler.CustomAuthenticationEntryPoint
import com.rookiefit.rookiefit.provider.JwtProvider
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtProvider: JwtProvider,
    private val userRepository: UserRepository,
    private val entryPoint: CustomAuthenticationEntryPoint
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(
                        "/",
                        "/api/auth/**",
                        "/error",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                    ).permitAll()
                    .requestMatchers("/api/user/**").hasRole("USER")
                    .anyRequest().authenticated()
            }
            .csrf { csrf ->
                csrf.disable() //테스트단계에서 비활성화
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .exceptionHandling {
                it.authenticationEntryPoint(entryPoint)
            }
            .addFilterBefore(JwtAuthenticationFilter(jwtProvider, userRepository), UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}