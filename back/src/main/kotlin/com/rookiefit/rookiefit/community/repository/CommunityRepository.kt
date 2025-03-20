package com.rookiefit.rookiefit.community.repository

import com.rookiefit.rookiefit.community.entity.CommunityEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface CommunityRepository: JpaRepository<CommunityEntity, Long> {

    fun findByCommunityType(
        communityType: String,
        pageable: Pageable
    ): Page<CommunityEntity>

    fun findByCommunityId(communityId: Long): CommunityEntity

    fun findByUserProfile_UserProfileId(
        userProfileId: Long,
        pageable: Pageable
    ): Page<CommunityEntity>

    fun findByUserProfile_UserProfileIdAndCommunityType(
        userProfileId: Long,
        communityType: String,
        pageable: Pageable
    ): Page<CommunityEntity>

    fun findByCommunityTitleContaining(
        query: String,
        pageable: Pageable
    ): Page<CommunityEntity>

    fun findByCommunityTitleContainingOrCommunityContentContaining(
        query1: String,
        query2: String,
        pageable: Pageable
    ): Page<CommunityEntity>

    fun findByCommunityAuthorContaining(
        query: String,
        pageable: Pageable
    ): Page<CommunityEntity>

    fun findByCommunityTypeAndCommunityTitleContaining(
        communityType: String,
        query: String,
        pageable: Pageable
    ): Page<CommunityEntity>

    fun findByCommunityTypeAndCommunityTitleContainingOrCommunityContentContaining(
        communityType: String,
        query1: String,
        query2: String,
        pageable: Pageable
    ): Page<CommunityEntity>

    fun findByCommunityTypeAndCommunityAuthorContaining(
        communityType: String,
        query: String,
        pageable: Pageable
    ): Page<CommunityEntity>

    fun findByUserProfile_UserProfileIdAndCommunityTitleContaining(
        userProfileId: Long,
        query: String,
        pageable: Pageable
    ): Page<CommunityEntity>

    fun findByUserProfile_UserProfileIdAndCommunityAuthorContaining(
        userProfileId: Long,
        query: String,
        pageable: Pageable
    ): Page<CommunityEntity>

    fun findByUserProfile_UserProfileIdAndCommunityTypeAndCommunityTitleContaining(
        userProfileId: Long,
        communityType: String,
        query: String,
        pageable: Pageable
    ): Page<CommunityEntity>

    fun findByUserProfile_UserProfileIdAndCommunityTypeAndCommunityAuthorContaining(
        userProfileId: Long,
        communityType: String,
        query: String,
        pageable: Pageable
    ): Page<CommunityEntity>
}