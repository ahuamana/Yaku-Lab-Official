package com.yakulab.usecases.ar

import com.ahuaman.data.dashboard.repository.ARRepository
import kotlinx.coroutines.flow.flow
import java.nio.ByteBuffer
import javax.inject.Inject

class DownloadModelInstanceUseCase @Inject constructor(
    private val arRepository: ARRepository
) {
    suspend operator fun invoke(category: String, modelName: String) = arRepository.downloadARModel(category, modelName)

}