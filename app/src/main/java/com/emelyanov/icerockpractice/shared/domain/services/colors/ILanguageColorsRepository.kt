package com.emelyanov.icerockpractice.shared.domain.services.colors

interface ILanguageColorsRepository {
    /**
     * @param hex - hex-color string
     * @return int-color.
     */
    fun getLangColor(language: String): Int?
}