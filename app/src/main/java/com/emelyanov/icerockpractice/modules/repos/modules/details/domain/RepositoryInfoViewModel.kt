package com.emelyanov.icerockpractice.modules.repos.modules.details.domain

import android.util.Log
import androidx.lifecycle.*
import com.emelyanov.icerockpractice.modules.auth.domain.AuthViewModel
import com.emelyanov.icerockpractice.modules.auth.domain.usecases.GetEnterTheTokenMessageUseCase
import com.emelyanov.icerockpractice.modules.repos.modules.details.domain.usecases.GetRepoDetailsUseCase
import com.emelyanov.icerockpractice.modules.repos.modules.details.domain.usecases.GetRepoReadmeUseCase
import com.emelyanov.icerockpractice.modules.repos.modules.details.domain.usecases.OpenUrlUseCase
import com.emelyanov.icerockpractice.modules.repos.modules.details.domain.usecases.ReplaceReadmeLocalUrisUseCase
import com.emelyanov.icerockpractice.modules.repos.modules.details.presentation.DetailInfoFragment
import com.emelyanov.icerockpractice.modules.repos.modules.details.utils.NavigationConsts
import com.emelyanov.icerockpractice.modules.repos.modules.list.domain.RepositoriesListViewModel
import com.emelyanov.icerockpractice.shared.domain.models.Repo
import com.emelyanov.icerockpractice.shared.domain.models.RepoDetails
import com.emelyanov.icerockpractice.shared.domain.models.RequestErrorType
import com.emelyanov.icerockpractice.shared.domain.models.RequestResult
import com.emelyanov.icerockpractice.shared.domain.usecases.GetConnectionErrorStringUseCase
import com.emelyanov.icerockpractice.shared.domain.usecases.GetInvalidTokenMessageUseCase
import com.emelyanov.icerockpractice.shared.domain.usecases.GetServerNotRespondingStringUseCase
import com.emelyanov.icerockpractice.shared.domain.usecases.GetUndescribedErrorMessageUseCase
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
    private val savedStateHandle: SavedStateHandle,
    private val getServerNotRespondingString: GetServerNotRespondingStringUseCase,
    private val getUndescribedErrorString: GetUndescribedErrorMessageUseCase,
    private val getInvalidTokenString: GetInvalidTokenMessageUseCase,
    private val openUrl: OpenUrlUseCase
) : ViewModel() {
    private val _state: MutableLiveData<State> = MutableLiveData(State.Loading)
    val state: LiveData<State>
        get() = _state

    init {
        loadInfo()
    }

    fun onLinkClick(url: String) = openUrl(url)

    fun loadInfo() {
        val owner = requireNotNull(savedStateHandle.get<String>(NavigationConsts.REPO_OWNER_KEY))
        val repo = requireNotNull(savedStateHandle.get<String>(NavigationConsts.REPO_NAME_KEY))

        viewModelScope.launch(Dispatchers.IO) {
            _state.postValue(State.Loading)

            getRepoDetails(owner, repo).let { result ->
                when(result) {
                    is RequestResult.Success -> {
                        val newState = State.Loaded(
                            githubRepo = result.data,
                            readmeState = ReadmeState.Loading
                        )

                        _state.postValue(newState)

                        loadReadme(newState)
                    }
                    is RequestResult.Error -> {
                        when(result.type) {
                            RequestErrorType.Unauthorized -> _state.postValue(State.Error(getInvalidTokenString()))
                            RequestErrorType.ServerNotResponding -> _state.postValue(State.Error(getServerNotRespondingString()))
                            RequestErrorType.ConnectionError -> _state.postValue(State.ConnectionError)
                            else -> _state.postValue(State.Error(result.message ?: "${getUndescribedErrorString()} ${result.exception?.let{it::class.java}}"))
                        }
                    }
                }
            }
        }
    }

    private suspend fun loadReadme(state: State.Loaded) {
        getRepoReadme(
            owner = state.githubRepo.owner,
            repo = state.githubRepo.name
        ).let { result ->
            when(result) {
                is RequestResult.Success -> {
                    replaceReadmeLocalUris(
                        owner = state.githubRepo.owner,
                        repo = state.githubRepo.name,
                        readme = result.data
                    ).let { processedReadmeResult ->
                        when(processedReadmeResult){
                            is RequestResult.Success -> {
                                _state.postValue(
                                    state.copy(
                                        readmeState = ReadmeState.Loaded(processedReadmeResult.data)
                                    )
                                )
                            }
                            is RequestResult.Error -> processReadmeErrors(processedReadmeResult, state)
                        }
                    }
                }
                is RequestResult.Error -> processReadmeErrors(result, state)
            }
        }
    }

    private fun processReadmeErrors(result: RequestResult.Error<String>, state: State.Loaded) {
        when(result.type) {
            RequestErrorType.NotFound -> _state.postValue(state.copy(readmeState = ReadmeState.Empty))
            RequestErrorType.Unauthorized -> _state.postValue(state.copy(readmeState = ReadmeState.Error(getInvalidTokenString())))
            RequestErrorType.ServerNotResponding -> _state.postValue(state.copy(readmeState = ReadmeState.Error(getServerNotRespondingString())))
            RequestErrorType.ConnectionError -> _state.postValue(state.copy(readmeState = ReadmeState.ConnectionError))
            else -> _state.postValue(
                state.copy(
                    readmeState = ReadmeState.Error(result.message ?: "${getUndescribedErrorString()} ${result.exception?.let{it::class.java}}")
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