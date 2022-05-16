package com.emelyanov.icerockpractice.shared.domain.utils

import com.emelyanov.icerockpractice.shared.domain.models.Repo
import com.emelyanov.icerockpractice.shared.domain.models.responses.RepositoryDetailsResponse
import com.emelyanov.icerockpractice.shared.domain.models.responses.RepositoryShortResponse

fun RepositoryShortResponse.toRepo()
= Repo(
    name = this.name,
    description = this.description ?: "",
    language = this.language ?: ""
)