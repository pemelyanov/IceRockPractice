package com.emelyanov.icerockpractice.shared.domain.services.keyvaluestorage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit

private const val STORAGE_KEY = "GIT_STORAGE_KEY"
private const val TOKEN_KEY = "GIT_TOKEN_KEY"

class KeyValueStorage(
    context: Context
) : IKeyValueStorage {
    private val storage = context.getSharedPreferences(STORAGE_KEY, MODE_PRIVATE)

    override var authToken: String?
        get() = storage.getString(TOKEN_KEY, null)
        set(value) = storage.edit().putString(TOKEN_KEY, value).apply()
}