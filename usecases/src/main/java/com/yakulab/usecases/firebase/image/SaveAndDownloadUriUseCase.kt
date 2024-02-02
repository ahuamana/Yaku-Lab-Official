package com.yakulab.usecases.firebase.image

import com.ahuaman.data.dashboard.providers.ImageProvider
import java.io.File
import javax.inject.Inject

class SaveAndDownloadUriUseCase @Inject constructor(
    private val imageProvider: ImageProvider
) {

    operator fun invoke(file: File) = imageProvider.saveAndDownloadUri(file)
}