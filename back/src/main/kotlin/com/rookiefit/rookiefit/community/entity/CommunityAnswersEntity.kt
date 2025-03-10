package com.rookiefit.rookiefit.community.entity

import com.rookiefit.rookiefit.workout.entity.WorkoutEntity
import jakarta.persistence.*

@Entity
@Table(name = "community_answers")
class CommunityAnswersEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var communityAnswersId : Long = 0,
    var communityAnswersContent: String = "",
    var communityAnswersAuthor: String = "",
    var communityAnswersCreatedAt: String = "",
    @ManyToOne
    @JoinColumn(name = "community_id")
    var community: CommunityEntity? = null
)