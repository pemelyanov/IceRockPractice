package com.emelyanov.icerockpractice.modules.repos.modules.details.domain.usecases

import android.util.Log
import com.emelyanov.icerockpractice.shared.domain.services.apprepository.AppRepository
import com.emelyanov.icerockpractice.shared.domain.services.apprepository.IAppRepository
import com.emelyanov.icerockpractice.shared.domain.utils.parseLocalUris
import javax.inject.Inject


/**
 * Replaces readme local image uri`s with global uri`s.
 * Example: "readme/image1.png" will be replaced by something like
 * "https://raw.githubusercontent.com/Owner/Repo/master/readme/image1.png"
 */
class ReplaceReadmeLocalUrisUseCase
@Inject
constructor(
    private val appRepository: IAppRepository
) {
    suspend operator fun invoke(owner:String, repo:String, readme: String): String {
        val uris = parseLocalUris(readme)

        val readmeBuilder = StringBuilder(readme)

        uris.forEach { localUri ->
            val globalUri = appRepository.getImageUrl(owner, repo, localUri.uri)

            //Start index of image block
            val startIndex = readmeBuilder.indexOf(localUri.entry)
            if(startIndex == -1) return@forEach

            //Length of image block
            val len = localUri.entry.length

            //Replacing local uri in image block with global uri
            val newImageBlock = localUri.entry.replace(localUri.uri, globalUri)

            //Replacing old image block with new
            readmeBuilder.replace(startIndex, startIndex + len, newImageBlock)
        }

        return readmeBuilder.toString()
    }
}