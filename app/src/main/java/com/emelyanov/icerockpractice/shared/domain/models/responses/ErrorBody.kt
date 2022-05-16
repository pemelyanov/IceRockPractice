package com.emelyanov.icerockpractice.shared.domain.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class ErrorBody(
    val message: String
)