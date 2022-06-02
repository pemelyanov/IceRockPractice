package com.emelyanov.icerockpractice.modules.repos.modules.details.domain.usecases

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class OpenUrlUseCase
@Inject
constructor(
    @ApplicationContext
    private val context: Context
) {
    operator fun invoke(url: String) {
        val uri = Uri.parse(url)
        Intent(Intent.ACTION_VIEW).apply {
            data = uri
            context.startActivity(this)
        }
    }
}