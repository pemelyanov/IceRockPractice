package com.emelyanov.icerockpractice.shared.domain.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class RepositoryShortResponse(
    val name: String,
    val description: String?,
    val language: String?
)