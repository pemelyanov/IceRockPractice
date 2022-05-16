package com.emelyanov.icerockpractice.modules.core.domain.usecases

import com.emelyanov.icerockpractice.navigation.core.CoreDestinations
import com.emelyanov.icerockpractice.navigation.core.CoreNavProvider
import javax.inject.Inject


class NavigateToAuthUseCase
@Inject
constructor(
    private val coreNavProvider: CoreNavProvider
) {
    operator fun invoke()
    = coreNavProvider.requestNavigate(CoreDestinations.Authentication)
}