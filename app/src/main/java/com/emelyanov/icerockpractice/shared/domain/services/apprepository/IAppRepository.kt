package com.emelyanov.icerockpractice.shared.domain.services.apprepository

import com.emelyanov.icerockpractice.shared.domain.models.Repo
import com.emelyanov.icerockpractice.shared.domain.models.RepoDetails
import com.emelyanov.icerockpractice.shared.domain.models.UserInfo

interface IAppRepository {
        suspend fun getRepositories(): List<Repo>

        suspend fun getRepository(owner: String, repo: String): RepoDetails

        suspend fun getRepositoryReadme(ownerName: String, repositoryName: String, branchName: String? = null): String

        suspend fun signIn(token: String): UserInfo

        fun logout()

        suspend fun getImageUrl(owner: String, repo: String, path: String): String
}