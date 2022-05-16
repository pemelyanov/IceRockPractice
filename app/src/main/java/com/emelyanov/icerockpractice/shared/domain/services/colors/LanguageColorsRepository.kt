package com.emelyanov.icerockpractice.shared.domain.services.colors

import android.content.Context
import android.graphics.Color
import com.emelyanov.icerockpractice.R
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class LanguageColorsRepository(
    context: Context
) : ILanguageColorsRepository{
    private val _colorsDict = context.resources.openRawResource(R.raw.language_colors).bufferedReader().use {
        it.readText()
    }.let {
        Json.decodeFromString<Map<String, String?>>(it)
    }

    override fun getLangColor(language: String): Int?
    = _colorsDict[language]?.let { Color.parseColor(it) }
}