package com.emelyanov.icerockpractice.modules.auth.domain

import androidx.lifecycle.ViewModel
import com.emelyanov.icerockpractice.navigation.core.CoreDestinations
import com.emelyanov.icerockpractice.navigation.core.CoreNavProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel
@Inject
constructor(
    private val coreNavProvider: CoreNavProvider
) : ViewModel() {
    fun authButtonClick() = coreNavProvider.requestNavigate(CoreDestinations.RepositoriesList)
}