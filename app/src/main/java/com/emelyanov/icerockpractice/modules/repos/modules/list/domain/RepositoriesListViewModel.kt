package com.emelyanov.icerockpractice.modules.repos.modules.list.domain

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emelyanov.icerockpractice.modules.auth.domain.AuthViewModel
import com.emelyanov.icerockpractice.modules.repos.modules.list.domain.usecases.GetReposUseCase
import com.emelyanov.icerockpractice.modules.repos.modules.list.domain.usecases.NavigateToDetailsUseCase
import com.emelyanov.icerockpractice.navigation.core.CoreDestinations
import com.emelyanov.icerockpractice.navigation.core.CoreNavProvider
import com.emelyanov.icerockpractice.shared.domain.models.Repo
import com.emelyanov.icerockpractice.shared.domain.usecases.GetInvalidTokenMessageUseCase
import com.emelyanov.icerockpractice.shared.domain.usecases.GetServerNotRespondingStringUseCase
import com.emelyanov.icerockpractice.shared.domain.usecases.GetUndescribedErrorMessageUseCase
import com.emelyanov.icerockpractice.shared.domain.utils.ConnectionErrorException
import com.emelyanov.icerockpractice.shared.domain.utils.ServerNotRespondingException
import com.emelyanov.icerockpractice.shared.domain.utils.UnauthorizedException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoriesListViewModel
@Inject
constructor(
    private val getRepos: GetReposUseCase,
    private val navigateToDetails: NavigateToDetailsUseCase,
    private val getServerNotRespondingString: GetServerNotRespondingStringUseCase,
    private val getUndescribedErrorString: GetUndescribedErrorMessageUseCase,
    private val getInvalidTokenString: GetInvalidTokenMessageUseCase
) : ViewModel() {
    private val _state: MutableLiveData<State> = MutableLiveData(State.Loading)
    val state: LiveData<State>
        get() = _state

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.postValue(State.Loading)
            try {
                getRepos().let {
                    if(it.isEmpty())
                        _state.postValue(State.Empty)
                    else
                        _state.postValue(
                            State.Loaded(
                                repos = it
                            )
                        )
                }
            } catch (ex: UnauthorizedException) {
                _state.postValue(State.Error(getInvalidTokenString()))
            } catch (ex: ServerNotRespondingException) {
                _state.postValue(State.Error(getServerNotRespondingString()))
            } catch (ex: ConnectionErrorException) {
                _state.postValue(State.ConnectionError)
            } catch (ex: Exception) {
                _state.postValue(State.Error(ex.message ?: "${getUndescribedErrorString()} ${ex::class.java}"))
            }
        }
    }

    fun onRepoClick(owner: String, repo: String) = navigateToDetails(owner, repo)

    sealed interface State {
        object Loading : State
        data class Loaded(val repos: List<Repo>) : State
        data class Error(val error: String) : State
        object ConnectionError : State
        object Empty : State
    }
}