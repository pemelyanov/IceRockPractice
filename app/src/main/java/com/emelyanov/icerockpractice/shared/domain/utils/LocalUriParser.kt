package com.emelyanov.icerockpractice.shared.domain.utils

import android.util.Log

/**
 * Finds local uri`s like "readme/image1.png" in markdown syntax document
 * and returns set of pairs: entry - uri
 * @return example: entry = "[image1.](readme/image1.png)", uri = "readme/image1.png"
 * or entry = "src='readme/image1.png'", uri = "readme/image1.png"
 */
fun parseLocalUris(document: String): Set<UriPair> {
    //Regex of local image path
    val innerRegex = Regex("""((\w*|[_,-,\,.]*)\/)*(\w*|[_,-,\,.])\.(png|jpg|jpeg|gif)""")

    //Regex of markdown syntax image
    val regex = Regex("""(src=\"${innerRegex.pattern}\")|(!\[.*\]\(${innerRegex.pattern}\))""")

    return regex.findAll(document).map {
        UriPair(
            entry = it.value,
            uri = innerRegex.find(it.value)!!.value
        )
    }.toSet()
}

data class UriPair(
    val entry: String,
    val uri: String
)