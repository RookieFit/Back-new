package com.rookiefit.rookiefit.diet.entity

import jakarta.persistence.*

@Entity
@Table(name = "user_diet")
class UserDietEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val userId: String,
    val dietDate: String,

    var totalCalories: Double = 0.0,

    @OneToMany(mappedBy = "userDiet", cascade = [CascadeType.ALL], orphanRemoval = true)
    var details: MutableList<UserDietDetailEntity> = mutableListOf()
) {
    // 음식 추가 시 totalcal 업데이트
    fun addDetail(detail: UserDietDetailEntity) {
        details.add(detail)
        updateTotalCalories()
    }

    // 음식 제거 시 totalcal 업데이트
    fun removeDetail(detail: UserDietDetailEntity) {
        details.remove(detail)
        updateTotalCalories()
    }

    // 총 칼로리 업데이트
    fun updateTotalCalories() {
        totalCalories = details.sumOf { it.enerc }
    }

    // @PostPersist 또는 @PostUpdate로 엔티티가 DB에 반영된 후 처리할 수도 있습니다
    @PostPersist
    @PostUpdate
    fun updateCaloriesAfterSaveOrUpdate() {
        updateTotalCalories()  // DB에 저장된 후 totalCalories를 갱신
    }
}