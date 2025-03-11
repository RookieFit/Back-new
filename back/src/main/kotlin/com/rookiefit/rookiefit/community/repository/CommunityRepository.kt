package com.rookiefit.rookiefit.community.repository

import com.rookiefit.rookiefit.community.entity.CommunityEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface CommunityRepository: JpaRepository<CommunityEntity, Long> {
    fun findByCommunityType(communityType: String, pageable: Pageable): Page<CommunityEntity>
}