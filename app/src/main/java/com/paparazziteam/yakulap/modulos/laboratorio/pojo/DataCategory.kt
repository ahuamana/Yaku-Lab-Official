package com.paparazziteam.yakulap.modulos.laboratorio.pojo

import com.paparazziteam.yakulap.helper.Constants
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


data class DataCategory (
    var Categorias: List<DataItems>? = null,
)

data class DataItems (
    var Challenge: List<DataChallenge>? = null,
)

@Serializable
data class DataChallenge (
    @SerialName("description")
    var description: String? = null,

    @SerialName("id")
    var id: String? = null,

    @SerialName("url")
    var url: String? = null,

    @SerialName("name")
    var name: String? = null,

    @SerialName("status")
    var status: Boolean? = null,

    @SerialName("category")
    var category: String? = null
)

data class DataListChallenge (
    var listInsectos: List<DataChallenge>? = null,
    var listAnimalsOther: List<DataChallenge>? = null,
    var listAnimalsDomestic: List<DataChallenge>? = null
)

enum class TypeCategoria(val value:String) {
    Insectos(Constants.CATEGORY_INSECTS),
    AnimalesDomesticos(Constants.CATEGORY_DOMESTIC_ANIMALS),
    AnimalesOthers(Constants.CATEGORY_OTHERS_ANIMALS)
}