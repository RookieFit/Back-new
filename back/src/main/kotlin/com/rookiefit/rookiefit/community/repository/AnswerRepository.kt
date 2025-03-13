package com.rookiefit.rookiefit.community.repository

import com.rookiefit.rookiefit.community.entity.CommunityAnswersEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface AnswerRepository: JpaRepository<CommunityAnswersEntity, Long> {
    fun findByCommunity_CommunityId(communityId: Long, pageable: Pageable): Page<CommunityAnswersEntity>
}