package com.emelyanov.icerockpractice.shared.domain.usecases

import android.content.Context
import com.emelyanov.icerockpractice.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class GetConnectionErrorStringUseCase
@Inject
constructor(
    @ApplicationContext
    private val context: Context
) {
    operator fun invoke() : String
    = context.getString(R.string.connection_error_message)
}