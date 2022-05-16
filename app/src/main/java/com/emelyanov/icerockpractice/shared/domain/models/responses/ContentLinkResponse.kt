package com.emelyanov.icerockpractice.shared.domain.models.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable()
data class ContentLinkResponse(
    @SerialName("download_link")
    val downloadLink: String
)