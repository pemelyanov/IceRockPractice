package com.emelyanov.icerockpractice.shared.domain.services.apprepository

import com.emelyanov.icerockpractice.shared.domain.models.Repo
import com.emelyanov.icerockpractice.shared.domain.models.RepoDetails
import com.emelyanov.icerockpractice.shared.domain.models.UserInfo
import com.emelyanov.icerockpractice.shared.domain.services.githubapi.IGitHubApi
import com.emelyanov.icerockpractice.shared.domain.services.keyvaluestorage.IKeyValueStorage
import com.emelyanov.icerockpractice.shared.domain.services.keyvaluestorage.KeyValueStorage
import com.emelyanov.icerockpractice.shared.domain.usecases.GetAuthHeaderUseCase
import com.emelyanov.icerockpractice.shared.domain.utils.gitRequestWrapper
import com.emelyanov.icerockpractice.shared.domain.utils.toRepo
import com.emelyanov.icerockpractice.shared.domain.utils.toRepoDetails
import com.emelyanov.icerockpractice.shared.domain.utils.toUserInfo

class AppRepository(
    private val gitHubApi: IGitHubApi,
    private val getAuthHeader: GetAuthHeaderUseCase,
    private val keyValueStorage: IKeyValueStorage
) : IAppRepository {
    override suspend fun getRepositories(): List<Repo>
    = gitRequestWrapper {
        gitHubApi.getRepositories(getAuthHeader())
    }.map {
        it.toRepo()
    }

    override suspend fun getRepository(repoId: String): RepoDetails
    = gitRequestWrapper {
        gitHubApi.getRepoDetails(
            token = getAuthHeader(),
            owner = keyValueStorage.userName ?: "",
            repo = repoId
        )
    }.toRepoDetails()

    override suspend fun getRepositoryReadme(
        ownerName: String,
        repositoryName: String,
        branchName: String
    ): String {
        TODO("Not yet implemented")
    }

    override suspend fun signIn(token: String): UserInfo
    = gitRequestWrapper {
        gitHubApi.getUserInfo("token $token")
    }.toUserInfo().also {
        keyValueStorage.authToken = token
        keyValueStorage.userName = it.login
    }

    override fun logout() {
        keyValueStorage.authToken = null
        keyValueStorage.userName = null
    }
}