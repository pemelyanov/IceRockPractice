package com.emelyanov.icerockpractice.modules.core.domain

import android.util.Log
import androidx.lifecycle.ViewModel
import com.emelyanov.icerockpractice.modules.core.domain.usecases.LogoutUseCase
import com.emelyanov.icerockpractice.modules.core.domain.usecases.NavigateToAuthUseCase
import com.emelyanov.icerockpractice.navigation.core.CoreNavProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CoreViewModel
@Inject
constructor(
    val coreNavProvider: CoreNavProvider,
    private val logout: LogoutUseCase,
    private val navigateToAuth: NavigateToAuthUseCase
) : ViewModel() {
    fun logoutClick() {
        logout()
        navigateToAuth()
    }
}