package com.emelyanov.icerockpractice.modules.repos.modules.list.domain.usecases

import com.emelyanov.icerockpractice.navigation.core.CoreDestinations
import com.emelyanov.icerockpractice.navigation.core.CoreNavProvider
import javax.inject.Inject


class NavigateToDetailsUseCase
@Inject
constructor(
    private val coreNavProvider: CoreNavProvider
) {
    operator fun invoke(owner: String, repo: String) {
        coreNavProvider.requestNavigate(CoreDestinations.RepositoryDetails(owner, repo))
    }
}