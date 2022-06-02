package com.emelyanov.icerockpractice.modules.repos.modules.details.domain

import android.util.Log
import androidx.lifecycle.*
import com.emelyanov.icerockpractice.modules.auth.domain.AuthViewModel
import com.emelyanov.icerockpractice.modules.repos.modules.details.domain.usecases.GetRepoDetailsUseCase
import com.emelyanov.icerockpractice.modules.repos.modules.details.domain.usecases.GetRepoReadmeUseCase
import com.emelyanov.icerockpractice.modules.repos.modules.details.domain.usecases.ReplaceReadmeLocalUrisUseCase
import com.emelyanov.icerockpractice.modules.repos.modules.details.presentation.DetailInfoFragment
import com.emelyanov.icerockpractice.modules.repos.modules.details.utils.NavigationConsts
import com.emelyanov.icerockpractice.modules.repos.modules.list.domain.RepositoriesListViewModel
import com.emelyanov.icerockpractice.shared.domain.models.Repo
import com.emelyanov.icerockpractice.shared.domain.models.RepoDetails
import com.emelyanov.icerockpractice.shared.domain.utils.ConnectionErrorException
import com.emelyanov.icerockpractice.shared.domain.utils.NotFoundException
import com.emelyanov.icerockpractice.shared.domain.utils.ServerNotRespondingException
import com.emelyanov.icerockpractice.shared.domain.utils.UnauthorizedException
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoryInfoViewModel
@Inject
constructor(
    private val getRepoDetails: GetRepoDetailsUseCase,
    private val getRepoReadme: GetRepoReadmeUseCase,
    private val replaceReadmeLocalUris: ReplaceReadmeLocalUrisUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state: MutableLiveData<State> = MutableLiveData(State.Loading)
    val state: LiveData<State>
        get() = _state

    init {
        loadInfo()
    }

    fun loadInfo() {
        val owner = requireNotNull(savedStateHandle.get<String>(NavigationConsts.REPO_OWNER_KEY))
        val repo = requireNotNull(savedStateHandle.get<String>(NavigationConsts.REPO_NAME_KEY))

        viewModelScope.launch(Dispatchers.IO) {
            _state.postValue(State.Loading)

            try {
                getRepoDetails(owner, repo).let { repoDetails ->

                    val newState = State.Loaded(
                        githubRepo = repoDetails,
                        readmeState = ReadmeState.Loading
                    )

                    _state.postValue(newState)

                    loadReadme(newState)
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

    private suspend fun loadReadme(state: State.Loaded) {
        try {
            getRepoReadme(
                owner = state.githubRepo.owner,
                repo = state.githubRepo.name
            ).let { markdown ->
                replaceReadmeLocalUris(
                    owner = state.githubRepo.owner,
                    repo = state.githubRepo.name,
                    readme = markdown
                ).let { processedReadme ->
                    _state.postValue(
                        state.copy(
                            readmeState = ReadmeState.Loaded(processedReadme)
                        )
                    )
                }
            }
        } catch (ex: NotFoundException) {
            _state.postValue(state.copy(readmeState = ReadmeState.Empty))
        } catch (ex: UnauthorizedException) {
            _state.postValue(state.copy(readmeState = ReadmeState.Error("Invalid token.")))
        } catch (ex: ServerNotRespondingException) {
            _state.postValue(state.copy(readmeState = ReadmeState.Error("Server not responding")))
        } catch (ex: ConnectionErrorException) {
            _state.postValue(state.copy(readmeState = ReadmeState.ConnectionError))
        } catch (ex: Exception) {
            _state.postValue(
                state.copy(
                    readmeState = ReadmeState.Error(ex.message ?: "Undescribed error: ${ex::class.java}")
                )
            )
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