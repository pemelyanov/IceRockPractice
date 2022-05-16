package com.emelyanov.icerockpractice.modules.repos.modules.details.domain

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emelyanov.icerockpractice.modules.auth.domain.AuthViewModel
import com.emelyanov.icerockpractice.modules.repos.modules.details.domain.usecases.GetRepoDetailsUseCase
import com.emelyanov.icerockpractice.modules.repos.modules.details.domain.usecases.GetRepoReadmeUseCase
import com.emelyanov.icerockpractice.modules.repos.modules.list.domain.RepositoriesListViewModel
import com.emelyanov.icerockpractice.shared.domain.models.Repo
import com.emelyanov.icerockpractice.shared.domain.models.RepoDetails
import com.emelyanov.icerockpractice.shared.domain.utils.ConnectionErrorException
import com.emelyanov.icerockpractice.shared.domain.utils.NotFoundException
import com.emelyanov.icerockpractice.shared.domain.utils.ServerNotRespondingException
import com.emelyanov.icerockpractice.shared.domain.utils.UnauthorizedException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoryInfoViewModel
@Inject
constructor(
    private val getRepoDetails: GetRepoDetailsUseCase,
    private val getRepoReadme: GetRepoReadmeUseCase
) : ViewModel() {
    private val _state: MutableLiveData<State> = MutableLiveData(State.Loading)
    val state: LiveData<State>
        get() = _state

    fun loadInfo(repoId: String) {
        viewModelScope.launch {
            _state.postValue(State.Loading)

            try {
                getRepoDetails(repoId).let {

                    val newState = State.Loaded(
                        githubRepo = it,
                        readmeState = ReadmeState.Loading
                    )

                    _state.postValue(newState)

                    Log.d("MarkdownError", "Before")
                    try {
                        getRepoReadme(
                            owner = newState.githubRepo.owner,
                            repo = newState.githubRepo.name
                        ).let { markdown ->
                                _state.postValue(newState.copy(
                                    readmeState = ReadmeState.Loaded(markdown)
                                )
                            )
                        }
                    } catch (ex: NotFoundException) {
                        _state.postValue(newState.copy(readmeState = ReadmeState.Empty))
                    } catch (ex: UnauthorizedException) {
                        _state.postValue(newState.copy(readmeState = ReadmeState.Error("Invalid token.")))
                    } catch (ex: ServerNotRespondingException) {
                        _state.postValue(newState.copy(readmeState = ReadmeState.Error("Server not responding")))
                    } catch (ex: ConnectionErrorException) {
                        _state.postValue(newState.copy(readmeState = ReadmeState.ConnectionError))
                    } catch (ex: Exception) {
                        _state.postValue(
                            newState.copy(
                                readmeState = ReadmeState.Error(ex.message ?: "Undescribed error: ${ex::class.java}")
                            )
                        )
                    }
                }
            } catch (ex: UnauthorizedException) {
                _state.postValue(State.Error("Invalid token."))
            } catch (ex: ServerNotRespondingException) {
                _state.postValue(State.Error("Server not responding..."))
            } catch (ex: ConnectionErrorException) {
                _state.postValue(State.ConnectionError)
            } catch (ex: Exception) {
                _state.postValue(State.Error(ex.message ?: "Undescribed error: ${ex::class.java}"))
            }
        }
    }

    sealed interface State {
        object Loading : State
        data class Error(val error: String) : State
        object ConnectionError : State
        data class Loaded(
            val githubRepo: RepoDetails,
            val readmeState: ReadmeState
        ) : State
    }

    sealed interface ReadmeState {
        object Loading : ReadmeState
        object Empty : ReadmeState
        data class Error(val error: String) : ReadmeState
        object ConnectionError : ReadmeState
        data class Loaded(val markdown: String) : ReadmeState
    }
}