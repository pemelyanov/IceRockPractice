package com.emelyanov.icerockpractice.navigation.core

sealed interface CoreDestinations {
    object Authentication : CoreDestinations
    object RepositoriesList : CoreDestinations
    data class RepositoryDetails(val owner: String, val repo: String) : CoreDestinations
}