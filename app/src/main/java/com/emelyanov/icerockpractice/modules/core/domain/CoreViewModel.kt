package com.emelyanov.icerockpractice.modules.core.domain

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emelyanov.icerockpractice.modules.core.domain.usecases.LogoutUseCase
import com.emelyanov.icerockpractice.modules.core.domain.usecases.NavigateToAuthUseCase
import com.emelyanov.icerockpractice.navigation.core.CoreNavProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoreViewModel
@Inject
constructor(
    val coreNavProvider: CoreNavProvider,
    private val logout: LogoutUseCase,
    private val navigateToAuth: NavigateToAuthUseCase
) : ViewModel() {

    private val _actions: Channel<Action> = Channel()
    val actions: Flow<Action>
        get() = _actions.receiveAsFlow()

    fun onLogoutClick() {
        viewModelScope.launch {
            _actions.send(
                Action.ShowExitDialog(
                    onSuccess = {
                        logout()
                        navigateToAuth()
                    }
                )
            )
        }
    }

    sealed interface Action {
        data class ShowExitDialog(val onSuccess: () -> Unit) : Action
    }
}