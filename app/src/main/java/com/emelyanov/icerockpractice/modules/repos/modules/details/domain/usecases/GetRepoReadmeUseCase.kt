package com.emelyanov.icerockpractice.modules.repos.modules.details.domain.usecases

import com.emelyanov.icerockpractice.shared.domain.services.apprepository.AppRepository
import com.emelyanov.icerockpractice.shared.domain.services.apprepository.IAppRepository
import javax.inject.Inject


class GetRepoReadmeUseCase
@Inject
constructor(
    private val appRepository: IAppRepository
) {
    suspend operator fun invoke(
        owner: String,
        repo: String
    ) : String
    = appRepository.getRepositoryReadme(
        ownerName = owner,
        repositoryName = repo
    )
}