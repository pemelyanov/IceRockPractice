package com.emelyanov.icerockpractice.shared.domain.models.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RepositoryDetailsResponse(
    @SerialName("full_name")
    val fullName: String,
    val license: License,
    @SerialName("stargazers_count")
    val stargazersCount: Int,
    @SerialName("forks_count")
    val forksCount: Int,
    @SerialName("watchers_count")
    val watchersCount: Int,
)
