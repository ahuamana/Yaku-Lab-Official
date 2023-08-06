package com.yakulab.domain.laboratory

import com.yakulab.domain.dashboard.ObservationEntity

data class DataCategory (
    var Categorias: List<DataItems>? = null,
)

data class DataItems (
    var Challenge: List<DataChallenge>? = null,
)

data class DataChallenge (
    var description: String? = null,
    var id: String? = null,
    var url: String? = null,
    var name: String? = null,
    var status: Boolean? = null,
    var category: String? = null,
    var image_parent: String? = null,
    var image_child: String? = null,
    var tittle: String? = null,
    var pointsToGive: Int? = null,
    var image_result: List<String>? = null,
    var text_result: List<String>? = null,

    var type: TypeChallenge? = null,
    var text_result_for_nerby_species: String? = null,
    var challenge_name: String? = null,
    var is_nearby_species: Boolean = false,
)

enum class TypeChallenge() {
    NEARBY_SPECIES,
    YAKULAB_PROPOSALS,
    TEACHER_PROPOSALS,
}

fun ObservationEntity.toDataChallengeNearbySpecies(): DataChallenge {
    return DataChallenge(
        description = this.id.toString(),
        id = this.id.toString(),
        url = this.identifications?.firstOrNull()?.taxon?.default_photo?.medium_url,
        name = this.identifications?.firstOrNull()?.taxon?.name,
        status = this.identifications?.firstOrNull()?.current,
        category = "null",
        type = TypeChallenge.NEARBY_SPECIES, // Nearby species Type
        is_nearby_species = true,
        text_result_for_nerby_species = this.identifications?.firstOrNull()?.taxon?.wikipedia_url,
        image_parent = this.identifications?.firstOrNull()?.taxon?.default_photo?.medium_url,
        image_child = this.identifications?.firstOrNull()?.taxon?.default_photo?.medium_url,
        tittle = this.identifications?.firstOrNull()?.taxon?.name,
        pointsToGive = randomInteger(),
        image_result = listOf(
            this.identifications?.firstOrNull()?.taxon?.default_photo?.medium_url?:""
        ),
        text_result = emptyList(),
        challenge_name = this.identifications?.firstOrNull()?.taxon?.name,
    )
}



fun randomInteger(): Int {
    return (0..10).random()
}

data class DataListChallenge (
    var listInsectos: List<DataChallenge>? = null,
    var listAnimalsOther: List<DataChallenge>? = null,
    var listAnimalsDomestic: List<DataChallenge>? = null
)

enum class TypeCategoria(val value:String) {
    Insectos(CATEGORY_INSECTS),
    AnimalesDomesticos(CATEGORY_DOMESTIC_ANIMALS),
    AnimalesOthers(CATEGORY_OTHERS_ANIMALS)
}

val CATEGORY_INSECTS = "Insectos"
val CATEGORY_DOMESTIC_ANIMALS = "Animales Domesticos"
val CATEGORY_OTHERS_ANIMALS = "Otros"