package com.rookiefit.rookiefit.community.repository

import com.rookiefit.rookiefit.community.entity.CommunityEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface CommunityRepository: JpaRepository<CommunityEntity, Long>, JpaSpecificationExecutor<CommunityEntity> {

    fun findByCommunityId(communityId: Long): CommunityEntity

}