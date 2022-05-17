package com.emelyanov.markdownview

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser

class MarkdownView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int) : WebView(context, attrs, defStyleAttr, defStyleRes) {
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    /**
     * Base HTML file with applied styles. To load body use ".replace("renderbody", content)"
     */
    private val baseHtml: String = resources.openRawResource(R.raw.base_html).bufferedReader().use {
        it.readText()
    }

    private val flavour = GFMFlavourDescriptor()

    init {
        with(settings) {
            domStorageEnabled = true
            blockNetworkLoads = false
            blockNetworkImage = false
            loadsImagesAutomatically = true
        }
        webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                context.startActivity(Intent(Intent.ACTION_VIEW, request?.url))
                return true
            }
        }
    }

    /**
     * Converts markdown to html and displays it with default dark github theme.
     */
    fun loadMarkdownText(markdown: String) {
        val tree = MarkdownParser(flavour).buildMarkdownTreeFromString(markdown)
        var html = HtmlGenerator(markdown, tree, flavour).generateHtml()

        html = html.removePrefix("<body>")
        html = html.removeSuffix("</body>")

        loadMarkdownHtml(html)
    }


    /**
     * Displays rendered to html markdown with default dark github theme.
     */
    fun loadMarkdownHtml(html: String)
    = loadDataWithBaseURL(null, baseHtml.replace("renderbody", html),"text/html", "utf-8", null)
}