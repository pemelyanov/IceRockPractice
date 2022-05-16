package com.emelyanov.icerockpractice.modules.auth.domain.usecases

import com.emelyanov.icerockpractice.shared.domain.services.apprepository.IAppRepository
import javax.inject.Inject


class SignInUseCase
@Inject
constructor(
    private val appRepository: IAppRepository
) {
    suspend operator fun invoke(token: String)
    = appRepository.signIn(token)
}