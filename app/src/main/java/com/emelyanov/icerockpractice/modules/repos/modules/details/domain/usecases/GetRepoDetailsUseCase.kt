package com.emelyanov.icerockpractice.modules.repos.modules.details.domain.usecases

import com.emelyanov.icerockpractice.shared.domain.models.RepoDetails
import com.emelyanov.icerockpractice.shared.domain.models.RequestResult
import com.emelyanov.icerockpractice.shared.domain.services.apprepository.IAppRepository
import javax.inject.Inject


class GetRepoDetailsUseCase
@Inject
constructor(
    private val appRepository: IAppRepository
) {
    suspend operator fun invoke(owner: String, repo: String) : RequestResult<RepoDetails>
    = appRepository.getRepository(owner, repo)
}