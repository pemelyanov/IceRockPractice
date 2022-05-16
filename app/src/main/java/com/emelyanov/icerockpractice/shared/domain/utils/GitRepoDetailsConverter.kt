package com.emelyanov.icerockpractice.shared.domain.utils

import com.emelyanov.icerockpractice.shared.domain.models.RepoDetails
import com.emelyanov.icerockpractice.shared.domain.models.responses.RepositoryDetailsResponse

fun RepositoryDetailsResponse.toRepoDetails()
= RepoDetails(
    url = this.htmlUrl,
    name = this.name,
    owner = this.owner.login,
    license = this.license?.name ?: "",
    stargazersCount = this.stargazersCount,
    forksCount = this.forksCount,
    watchersCount = this.watchersCount
)