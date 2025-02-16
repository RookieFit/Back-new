package com.rookiefit.rookiefit.provider

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.SignatureAlgorithm
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.Date
import javax.crypto.spec.SecretKeySpec

@Component
class JwtProvider(@Value("\${secretKey}") private val secretKey: String) {
    private val logger = LoggerFactory.getLogger(JwtProvider::class.java)
    private val expirationTime: Long = 3600000

    //접근 토큰 생성
    fun generateAccessToken(userId: String) : String {
        val key = SecretKeySpec(secretKey.toByteArray(), SignatureAlgorithm.HS256.jcaName)
        return Jwts.builder()
            .setSubject(userId)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + expirationTime))
            .signWith(key)
            .compact()
    }

    //리프레시 토큰 생성
    fun generateRefreshToken(userId: String) : String {
        val key = SecretKeySpec(secretKey.toByteArray(), SignatureAlgorithm.HS256.jcaName)
        return Jwts.builder()
            .setSubject(userId)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 604800000)) // 7주일
            .signWith(key)
            .compact()
    }

    //토큰에서 유저아이디 추출
    fun extractUserId(token: String): String? {
        return getClaims(token).subject
    }

    //토큰 유효성 검사
    fun validateToken(token: String): Boolean {
        return  try {
            getClaims(token)
            true
        }catch (e: Exception) {
            handleJwtException(e)
        }
    }

    private fun getClaims(token: String): Claims {
        val key: Key = SecretKeySpec(secretKey.toByteArray(), "HmacSHA256")
        val jwtParser: JwtParser = Jwts.parserBuilder().setSigningKey(key).build()
        return jwtParser.parseClaimsJws(token).body
    }

    private fun handleJwtException(e: Exception): Boolean {
        when (e) {
            is io.jsonwebtoken.ExpiredJwtException -> {
                println("만료된 토큰: ${e.message}")
                logger.info("만료된 토큰: {}", e.message)
            }
            is io.jsonwebtoken.UnsupportedJwtException -> {
                println("형식과 맞지 않는 토큰: ${e.message}")
                logger.warn("형식과 맞지 않는 토큰: {}", e.message)
            }
            is io.jsonwebtoken.MalformedJwtException -> {
                println("구조적 문제가 있는 토큰: ${e.message}")
                logger.warn("구조적 문제가 있는 토큰: {}", e.message)
            }
            is io.jsonwebtoken.security.SignatureException -> {
                println("서명이 일치하지 않음: ${e.message}")
                logger.error("서명이 일치하지 않음: {}", e.message)
            }
            else -> {
                println("알 수 없는 JWT 오류: ${e.message}")
                logger.error("알 수 없는 JWT 오류: {}", e.message, e)
            }
        }
        return false
    }
}