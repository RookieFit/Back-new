package com.rookiefit.rookiefit.community.service

import com.rookiefit.rookiefit.auth.dto.ResponseDTO
import com.rookiefit.rookiefit.common.DateFormat
import com.rookiefit.rookiefit.community.dto.request.AnswerRequestDTO
import com.rookiefit.rookiefit.community.dto.response.AnswerResponseDTO
import com.rookiefit.rookiefit.community.entity.CommunityAnswersEntity
import com.rookiefit.rookiefit.community.repository.AnswerRepository
import com.rookiefit.rookiefit.community.repository.CommunityRepository
import com.rookiefit.rookiefit.user.repository.UserProfileRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class AnswerService(
    private val profileRepository: UserProfileRepository,
    private val answerRepository: AnswerRepository,
    private val communityRepository: CommunityRepository
) {
    fun createAnswer(currrentUserId: String?, communityId: Long, answerRequestDTO: AnswerRequestDTO): ResponseDTO {
        val profileUserEntity = profileRepository.findByUser_UserId(currrentUserId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저의 프로필이 존재하지 않습니다")
        val communityEntity = communityRepository.findByCommunityId(communityId)
        val communityAnswersEntity = CommunityAnswersEntity(
            communityAnswersAuthor = profileUserEntity.userProfileNickname,
            communityAnswersContent = answerRequestDTO.communityAnswersContent,
            communityAnswersCreatedAt = DateFormat.now(),
            community = communityEntity
        )
        answerRepository.save(communityAnswersEntity)
        return ResponseDTO("CREATE_ANSWER_SUCCESS", "댓글 작성 완료")
    }
    fun getAnswers(communityId: Long, page: Int, size: Int): Map<String, Any> {
        val pageable: Pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("communityAnswersCreatedAt")))
        val answersPage: Page<CommunityAnswersEntity> = answerRepository.findByCommunity_CommunityId(communityId, pageable)
        val answersDtoList = answersPage.content.map {
            AnswerResponseDTO(
                communityAnswersId = it.communityAnswersId,
                communityAnswersAuthor = it.communityAnswersAuthor,
                communityAnswersContent = it.communityAnswersContent,
                communityAnswersCreatedAt = it.communityAnswersCreatedAt
            )
        }
        return mapOf(
            "content" to answersDtoList,
            "totalPages" to answersPage.totalPages
        )
    }
}