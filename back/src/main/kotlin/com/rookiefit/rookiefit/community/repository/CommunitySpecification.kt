package com.rookiefit.rookiefit.community.repository

import com.rookiefit.rookiefit.community.entity.CommunityEntity
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification

object CommunitySpecification {
    fun filterBy(
        communityType: String?,
        userProfileId: Long?,
        titleQuery: String?,
        authorQuery: String?
    ): Specification<CommunityEntity> {
        return Specification { root, query, cb ->
            val predicates = mutableListOf<Predicate>()

            if (!communityType.isNullOrBlank() && communityType != "전체") {
                predicates.add(cb.equal(root.get<String>("communityType"), communityType))
            }

            if (userProfileId != null) {
                predicates.add(cb.equal(root.get<Long>("userProfile").get<Long>("userProfileId"), userProfileId))
            }

            titleQuery?.let {
                predicates.add(cb.like(root.get("communityTitle"), "%$it%"))
            }

            authorQuery?.let {
                predicates.add(cb.like(root.get("communityAuthor"), "%$it%"))
            }

            cb.and(*predicates.toTypedArray())
        }
    }
}