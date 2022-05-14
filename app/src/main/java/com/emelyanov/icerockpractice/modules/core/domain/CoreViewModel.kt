package com.emelyanov.icerockpractice.modules.core.domain

import android.util.Log
import androidx.lifecycle.ViewModel
import com.emelyanov.icerockpractice.navigation.core.CoreNavProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CoreViewModel
@Inject
constructor(
    val coreNavProvider: CoreNavProvider
) : ViewModel() {
    fun logoutClick() {
        Log.d("menu", "clicked")
    }
}