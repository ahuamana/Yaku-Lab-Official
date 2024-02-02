package com.yakulab.usecases.firebase.image

import com.ahuaman.data.dashboard.providers.ImageProvider
import javax.inject.Inject

class GetDownloadUriUseCase @Inject constructor(
    private val imageProvider: ImageProvider
) {
    fun invoke() = imageProvider.getDownloadUriFlow()
}