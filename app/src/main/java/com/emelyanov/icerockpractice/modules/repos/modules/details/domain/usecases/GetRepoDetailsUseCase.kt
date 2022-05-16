package com.emelyanov.icerockpractice.modules.repos.modules.details.domain.usecases

import com.emelyanov.icerockpractice.shared.domain.models.RepoDetails
import com.emelyanov.icerockpractice.shared.domain.services.apprepository.IAppRepository
import javax.inject.Inject


class GetRepoDetailsUseCase
@Inject
constructor(
    private val appRepository: IAppRepository
) {
    suspend operator fun invoke(repoId: String) : RepoDetails
    = appRepository.getRepository(repoId)
}