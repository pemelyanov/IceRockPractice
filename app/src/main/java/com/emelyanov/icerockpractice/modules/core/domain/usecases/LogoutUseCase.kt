package com.emelyanov.icerockpractice.modules.core.domain.usecases

import com.emelyanov.icerockpractice.shared.domain.services.apprepository.IAppRepository
import javax.inject.Inject


class LogoutUseCase
@Inject
constructor(
    private val appRepository: IAppRepository
) {
    operator fun invoke()
    = appRepository.logout()
}