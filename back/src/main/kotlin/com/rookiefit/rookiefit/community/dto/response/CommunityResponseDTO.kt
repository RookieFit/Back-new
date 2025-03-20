package com.rookiefit.rookiefit.community.dto.response

data class CommunityResponseDTO(
    val communityId: Long,
    val communityType: String,
    val communityTitle: String,
    val communityAuthor: String,
    val communityCreatedAt: String,
    val communityUpdatedAt: String = ""
)
