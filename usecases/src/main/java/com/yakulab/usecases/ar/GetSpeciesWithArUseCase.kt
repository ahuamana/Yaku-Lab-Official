package com.yakulab.usecases.ar

import com.paparazziteam.yakulab.binding.utils.createUUID
import com.yakulab.domain.dashboard.ItemSpecieAR
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSpeciesWithArUseCase @Inject constructor(){

    fun invoke() = flow{

        val list:List<ItemSpecieAR> = listOf(
            ItemSpecieAR(
                uuid = createUUID(),
                name = "Hormiga",
                urlModel = "https://ahuamana.github.io/models-ar/insect/ant.glb",
                preview = "https://ahuamana.github.io/models-ar/insect/ant.jpeg"
            )
        )
        emit(list)
    }
}