package com.rookiefit.rookiefit.community.repository

import com.rookiefit.rookiefit.community.entity.CommunityEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CommunityRepository: JpaRepository<CommunityEntity, Long> {
}