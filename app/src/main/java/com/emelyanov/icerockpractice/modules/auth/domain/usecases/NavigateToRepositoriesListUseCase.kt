package com.emelyanov.icerockpractice.modules.auth.domain.usecases

import com.emelyanov.icerockpractice.navigation.core.CoreDestinations
import com.emelyanov.icerockpractice.navigation.core.CoreNavProvider
import javax.inject.Inject


class NavigateToRepositoriesListUseCase
@Inject
constructor(
    private val coreNavProvider: CoreNavProvider
) {
    operator fun invoke()
    = coreNavProvider.requestNavigate(CoreDestinations.RepositoriesList)
}