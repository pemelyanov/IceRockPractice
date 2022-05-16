package com.emelyanov.icerockpractice.shared.domain.usecases

import com.emelyanov.icerockpractice.shared.domain.services.keyvaluestorage.IKeyValueStorage
import javax.inject.Inject


class GetAuthHeaderUseCase
@Inject
constructor(
    private val keyValueStorage: IKeyValueStorage
) {
    operator fun invoke() : String
    = "token ${keyValueStorage.authToken}"
}