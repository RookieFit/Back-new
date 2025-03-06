package com.rookiefit.rookiefit.diet.entity

import jakarta.persistence.*

@Entity
@Table(name = "user_diet_detail")
class UserDietDetailEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    var foodName: String,  // 음식 이름
    var foodFirstCategory: String,
    var chocdf: Double,  // 탄수화물
    var prot: Double,    // 단백질
    var fatce: Double,   // 지방
    var enerc: Double,   // 칼로리

    @Column(name = "diet_date")
    var dietDate: String,

    @ManyToOne
    @JoinColumn(name = "user_diet_id")
    var userDiet: UserDietEntity
) {

    fun setFoodInfo(foodInfo: FoodInfoEntity) {
        this.foodName = foodInfo.foodName
        this.foodFirstCategory = foodInfo.foodFirstCategory
        this.chocdf = foodInfo.chocdf
        this.prot = foodInfo.prot
        this.fatce = foodInfo.fatce
        this.enerc = foodInfo.enerc
        this.dietDate = userDiet.dietDate
    }
}
