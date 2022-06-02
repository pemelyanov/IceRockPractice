package com.emelyanov.icerockpractice.modules.auth.domain.usecases

import android.content.Context
import com.emelyanov.icerockpractice.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class GetEnterTheTokenMessageUseCase
@Inject
constructor(
    @ApplicationContext
    private val context: Context
) {
    operator fun invoke() : String
    = context.getString(R.string.enter_the_token_message)
}