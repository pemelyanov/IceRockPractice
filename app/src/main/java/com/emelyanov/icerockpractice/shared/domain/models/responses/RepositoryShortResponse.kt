package com.emelyanov.icerockpractice.shared.domain.models.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RepositoryShortResponse(
    val name: String,
    val owner: UserInfoResponse,
    val description: String?,
    val language: String?
)