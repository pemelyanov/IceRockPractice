package com.emelyanov.icerockpractice.shared.domain.services.apprepository

import com.emelyanov.icerockpractice.shared.domain.models.Repo
import com.emelyanov.icerockpractice.shared.domain.models.RepoDetails
import com.emelyanov.icerockpractice.shared.domain.models.RequestResult
import com.emelyanov.icerockpractice.shared.domain.models.UserInfo

interface IAppRepository {
        suspend fun getRepositories(): RequestResult<List<Repo>>

        suspend fun getRepository(owner: String, repo: String): RequestResult<RepoDetails>

        suspend fun getRepositoryReadme(ownerName: String, repositoryName: String, branchName: String? = null): RequestResult<String>

        suspend fun signIn(token: String): RequestResult<UserInfo>

        fun logout()

        suspend fun getImageUrl(owner: String, repo: String, path: String): RequestResult<String>
}