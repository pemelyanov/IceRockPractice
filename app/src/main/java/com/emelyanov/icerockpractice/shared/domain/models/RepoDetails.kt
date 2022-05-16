package com.emelyanov.icerockpractice.shared.domain.models

import com.emelyanov.icerockpractice.shared.domain.models.responses.License
import kotlinx.serialization.SerialName

data class RepoDetails (
    val url: String,
    val owner: String,
    val name: String,
    val license: String,
    val stargazersCount: Int,
    val forksCount: Int,
    val watchersCount: Int,
)