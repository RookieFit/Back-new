package com.rookiefit.rookiefit.diet.dto.request

import com.rookiefit.rookiefit.diet.dto.UserDietDetailDto

data class AddFoodToDietRequest(
    val userId: String,
    val dietDate: String,
    val dietDetails: List<UserDietDetailDto>
)