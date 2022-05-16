package com.emelyanov.icerockpractice.modules.auth.domain.usecases

import com.emelyanov.icerockpractice.shared.domain.services.keyvaluestorage.IKeyValueStorage
import javax.inject.Inject


class GetTokenUseCase
@Inject
constructor(
    private val keyValueStorage: IKeyValueStorage
) {
    operator fun invoke(): String?
    = keyValueStorage.authToken
}