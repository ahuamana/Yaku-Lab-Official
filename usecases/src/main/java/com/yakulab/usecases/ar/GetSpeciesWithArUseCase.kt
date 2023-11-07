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
                name = "Célula vegetal",
                urlModel = "https://ahuamana.github.io/models-ar/biology/plant-cell.glb",
                preview = "https://ahuamana.github.io/models-ar/biology/plant-cell.jpeg",
                scaleInUnit = 0.01f
            ),
            //abdomen-anatomy.glb
            ItemSpecieAR(
                uuid = createUUID(),
                name = "Abdomen Anatomía",
                urlModel = "https://ahuamana.github.io/models-ar/human/abdomen-anatomy.glb",
                preview = "https://ahuamana.github.io/models-ar/human/abdomen-anatomy.jpeg",
                scaleInUnit = 2f
            ),

            ItemSpecieAR(
                uuid = createUUID(),
                name = "Hormiga",
                urlModel = "https://ahuamana.github.io/models-ar/insect/ant.glb",
                preview = "https://ahuamana.github.io/models-ar/insect/ant.jpeg"
            ),
            ItemSpecieAR(
                uuid = createUUID(),
                name = "Abeja",
                urlModel = "https://ahuamana.github.io/models-ar/insect/animated-bee-flying.glb",
                preview = "https://ahuamana.github.io/models-ar/insect/animated-bee-flying.jpeg"
            ),
            ItemSpecieAR(
                uuid = createUUID(),
                name = "Gecko",
                urlModel = "https://ahuamana.github.io/models-ar/animal/gecko.glb",
                preview = "https://ahuamana.github.io/models-ar/animal/gecko.jpeg",
                scaleInUnit = 0.01f
            ),
        )
        emit(list)
    }
}