package com.rookiefit.rookiefit.community.entity

import com.rookiefit.rookiefit.user.entity.UserProfileEntity
import com.rookiefit.rookiefit.workout.entity.WorkoutDetailEntity
import jakarta.persistence.*

@Entity
@Table(name = "community")
class CommunityEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var communityId: Long = 0,
    var communityAuthor: String = "",
    var communityTitle: String = "",
    @Column(columnDefinition = "TEXT")
    var communityContent: String = "",
    var communityType: String = "",
    var communityCreatedAt: String = "",
    var communityUpdatedAt: String = "",
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_profile_id")
    var userProfile: UserProfileEntity?,
    @OneToMany(mappedBy = "community", cascade = [(CascadeType.ALL)], orphanRemoval = true)
    var communityAnswers: MutableList<CommunityAnswersEntity> = mutableListOf(),
)