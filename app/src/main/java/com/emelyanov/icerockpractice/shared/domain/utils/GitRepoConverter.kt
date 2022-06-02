package com.emelyanov.icerockpractice.shared.domain.utils

import com.emelyanov.icerockpractice.shared.domain.models.Repo
import com.emelyanov.icerockpractice.shared.domain.models.responses.RepositoryShortResponse

fun RepositoryShortResponse.toRepo()
= Repo(
    name = this.name,
    owner = this.owner.login,
    description = this.description ?: "",
    language = this.language ?: "",
    color = null
)