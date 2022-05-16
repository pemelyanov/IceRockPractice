package com.emelyanov.icerockpractice.shared.domain.models.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReadmeResponse (
    val content: String
)