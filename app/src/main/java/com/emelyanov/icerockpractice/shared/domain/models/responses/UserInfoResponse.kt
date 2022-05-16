package com.emelyanov.icerockpractice.shared.domain.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponse(
    val login: String
)