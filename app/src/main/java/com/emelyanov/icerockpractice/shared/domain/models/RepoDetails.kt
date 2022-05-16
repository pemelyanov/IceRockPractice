package com.emelyanov.icerockpractice.shared.domain.models

import com.emelyanov.icerockpractice.shared.domain.models.responses.License
import kotlinx.serialization.SerialName

data class RepoDetails (
    val fullName: String,
    val license: String,
    val stargazersCount: Int,
    val forksCount: Int,
    val watchersCount: Int,
)