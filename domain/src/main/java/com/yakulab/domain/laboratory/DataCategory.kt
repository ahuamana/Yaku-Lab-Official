package com.yakulab.domain.laboratory

import com.yakulab.domain.dashboard.ObservationEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DataCategory (
    @SerialName("Categorias")
    val Categorias: List<DataItems>? = null,
)

@Serializable
data class DataItems (
    @SerialName("Challenge")
    val Challenge: List<DataChallenge>? = null,
)

@Serializable
data class DataChallenge (
    @SerialName("id") var id: String? = null,
    @SerialName("description") var description: String? = null,
    @SerialName("url") var url: String? = null,
    @SerialName("name") var name: String? = null,
    @SerialName("status") var status: Boolean? = null,
    @SerialName("category") var category: String? = null,
    @SerialName("image_parent") var image_parent: String? = null,
    @SerialName("image_child") var image_child: String? = null,
    @SerialName("tittle") var tittle: String? = null,
    @SerialName("pointsToGive") var pointsToGive: Int? = null,
    @SerialName("image_result") var image_result: List<String>? = null,
    @SerialName("text_result") var text_result: List<String>? = null,
    @SerialName("type") var type: TypeChallenge? = null,
    @SerialName("text_result_for_nerby_species") var text_result_for_nerby_species: String? = null,
    @SerialName("challenge_name") var challenge_name: String? = null,
    @SerialName("is_nearby_species") var is_nearby_species: Boolean = false,
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

@Serializable
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