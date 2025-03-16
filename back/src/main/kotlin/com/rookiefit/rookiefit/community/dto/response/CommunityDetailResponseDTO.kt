package com.rookiefit.rookiefit.community.dto.response

data class CommunityDetailResponseDTO(
    val communityType: String,
    val communityTitle: String,
    val communityAuthor: String,
    val communityCreatedAt: String,
    val communityContent: String,
    val userProfileId: Long,
    val communityUpdatedAt: String = "",
)
