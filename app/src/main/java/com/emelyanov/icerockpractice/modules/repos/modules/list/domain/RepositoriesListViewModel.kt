package com.emelyanov.icerockpractice.modules.repos.modules.list.domain

import androidx.lifecycle.ViewModel
import com.emelyanov.icerockpractice.navigation.core.CoreDestinations
import com.emelyanov.icerockpractice.navigation.core.CoreNavProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RepositoriesListViewModel
@Inject
constructor(
    private val coreNavProvider: CoreNavProvider
) : ViewModel() {
    fun repoClick() = coreNavProvider.requestNavigate(CoreDestinations.RepositoryDetails(""))
}