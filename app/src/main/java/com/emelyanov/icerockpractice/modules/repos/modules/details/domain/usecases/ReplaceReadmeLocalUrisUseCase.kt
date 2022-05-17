package com.emelyanov.icerockpractice.modules.repos.modules.details.domain.usecases

import android.util.Log
import com.emelyanov.icerockpractice.shared.domain.services.apprepository.AppRepository
import com.emelyanov.icerockpractice.shared.domain.services.apprepository.IAppRepository
import com.emelyanov.icerockpractice.shared.domain.utils.parseLocalUris
import javax.inject.Inject


class ReplaceReadmeLocalUrisUseCase
@Inject
constructor(
    private val appRepository: IAppRepository
) {
    suspend operator fun invoke(owner:String, repo:String, readme: String): String {
        val uris = parseLocalUris(readme)

        var readmeBuilder = readme

        uris.forEach { localUri ->
            val globalUri = appRepository.getImageUrl(owner, repo, localUri.uri)

            readmeBuilder = readmeBuilder.replace(localUri.entry, localUri.entry.replace(localUri.uri, globalUri))
        }

        return readmeBuilder
    }
}