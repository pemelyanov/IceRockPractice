package com.emelyanov.icerockpractice.modules.repos.modules.list.domain.usecases

import com.emelyanov.icerockpractice.shared.domain.models.Repo
import com.emelyanov.icerockpractice.shared.domain.services.apprepository.IAppRepository
import javax.inject.Inject


class GetReposUseCase
@Inject
constructor(
    private val appRepository: IAppRepository
) {
    suspend operator fun invoke() : List<Repo>
    = appRepository.getRepositories()
}