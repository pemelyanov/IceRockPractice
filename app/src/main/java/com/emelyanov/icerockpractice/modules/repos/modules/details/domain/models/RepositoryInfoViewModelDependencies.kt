package com.emelyanov.icerockpractice.modules.repos.modules.details.domain.models

import com.emelyanov.icerockpractice.modules.repos.modules.details.domain.usecases.GetRepoDetailsUseCase
import com.emelyanov.icerockpractice.modules.repos.modules.details.domain.usecases.GetRepoReadmeUseCase
import com.emelyanov.icerockpractice.modules.repos.modules.details.domain.usecases.ReplaceReadmeLocalUrisUseCase
import javax.inject.Inject

class RepositoryInfoViewModelDependencies
@Inject
constructor(
    val getRepoDetails: GetRepoDetailsUseCase,
    val getRepoReadme: GetRepoReadmeUseCase,
    val replaceReadmeLocalUris: ReplaceReadmeLocalUrisUseCase
)