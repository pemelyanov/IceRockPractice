package com.emelyanov.icerockpractice.shared.domain.services.apprepository

import android.util.Base64
import android.util.Log
import com.emelyanov.icerockpractice.shared.domain.models.Repo
import com.emelyanov.icerockpractice.shared.domain.models.RepoDetails
import com.emelyanov.icerockpractice.shared.domain.models.UserInfo
import com.emelyanov.icerockpractice.shared.domain.services.colors.ILanguageColorsRepository
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
    private val keyValueStorage: IKeyValueStorage,
    private val colorsRepository: ILanguageColorsRepository
) : IAppRepository {
    override suspend fun getRepositories(): List<Repo>
    = gitRequestWrapper {
        gitHubApi.getRepositories(getAuthHeader())
    }.map { repoResponse ->
        repoResponse.toRepo().let { repo ->
            if(repo.language.isEmpty()) return@let repo
            repo.copy(color = colorsRepository.getLangColor(repo.language))
        }
    }

    override suspend fun getRepository(owner: String, repo: String): RepoDetails
    = gitRequestWrapper {
        gitHubApi.getRepoDetails(
            token = getAuthHeader(),
            owner = owner,
            repo = repo
        )
    }.toRepoDetails()

    override suspend fun getRepositoryReadme(
        ownerName: String,
        repositoryName: String,
        branchName: String
    ): String {
        return gitRequestWrapper {
            if(branchName.isEmpty())
                gitHubApi.getReadme(
                    token = getAuthHeader(),
                    owner = ownerName,
                    repo = repositoryName
                )
            else
                gitHubApi.getReadme(
                    token = getAuthHeader(),
                    owner = ownerName,
                    repo = repositoryName,
                    branch = branchName
                )
        }.let {
            Base64.decode(it.content, Base64.DEFAULT).decodeToString()
        }
    }

    override suspend fun signIn(token: String): UserInfo
    = gitRequestWrapper {
        gitHubApi.getUserInfo("token $token")
    }.toUserInfo().also {
        keyValueStorage.authToken = token
    }

    override fun logout() {
        keyValueStorage.authToken = null
    }

    override suspend fun getImageUrl(owner: String, repo: String, path: String): String
    = gitRequestWrapper {
        gitHubApi.getContent(
            token = getAuthHeader(),
            owner = owner,
            repo = repo,
            path = path
        )
    }.downloadUrl
}