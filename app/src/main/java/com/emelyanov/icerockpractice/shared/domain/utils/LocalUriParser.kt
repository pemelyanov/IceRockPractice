package com.emelyanov.icerockpractice.shared.domain.utils

import android.util.Log

fun parseLocalUris(document: String): List<UriPair> {
    val innerRegex = Regex("""((\w*|[_,-,\,.]*)\/)*(\w*|[_,-,\,.])\.(png|jpg|jpeg|gif)""")
    val regex = Regex("""(src=\"${innerRegex.pattern}\")|(!\[.*\]\(${innerRegex.pattern}\))""")
    return regex.findAll(document).map {
        Log.d("Uri parse", it.value)
        UriPair(
            entry = it.value,
            uri = innerRegex.find(it.value)!!.value
        )
    }.toList()
}

data class UriPair(
    val entry: String,
    val uri: String
)