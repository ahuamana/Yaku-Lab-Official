package com.yakulab.domain.dashboard

data class ObservationResponse(
    val total_results: Int,
    val page: Int,
    val per_page: Int,
    val results: List<Observation>
)

data class ObservationItemResponse(
    val total_results: Int,
    val page: Int,
    val per_page: Int,
    val results: List<Observation>
)

data class ObservationItem(
    val id:Long?,
    val taxon: Taxon? = null
)



data class Observation(
    val id:Long?,
    val uuid: String?,
    val created_at: String?,
    val location: String?,
    val spam: Boolean?,
    val identifications : List<Identification?>,
    val photos: List<Photo?>,
    val taxon: Taxon? = null
)

data class Photo(
    val id: Long?,
    val url: String?,
    val attribution: String?,
    val license_code: String?,
)

data class Identification(
    val id: Long?,
    val uuid: String?,
    val created_at: String?,
    val current: Boolean?,
    val taxon: Taxon?,
)

data class Taxon(
    val id: Long?,
    val uuid: String?,
    val name: String?,
    val preferred_common_name: String?,
    val iconic_taxon_name: String?,
    val rank: String?,
    val ancestor_ids: List<Int?>,
    val wikipedia_url: String?,
    val extinct: Boolean?,
    val universal_search_rank: Int?,
    val default_photo: DefaultPhoto?,
    val wikipedia_summary: String? = null,
)

data class DefaultPhoto(
    val url : String?,
    val square_url: String?,
    val medium_url: String?,
    val attribution: String?,
)

//Entities Adapters
data class ObservationEntity(
    val id:Long?,
    val uuid: String?,
    val created_at: String?,
    val location: String?,
    val spam: Boolean?,
    val identifications : List<IdentificationEntity?>,
    val photos: List<PhotoEntity?>,
    val taxon: TaxonEntity? = null
)

data class PhotoEntity(
    val id: Long?,
    val url: String?,
    val attribution: String?,
    val license_code: String?,
)

data class IdentificationEntity(
    val id: Long?,
    val uuid: String?,
    val created_at: String?,
    val current: Boolean?,
    val taxon: TaxonEntity?,
)

data class TaxonEntity(
    val id: Long?,
    val uuid: String?,
    val name: String?,
    val preferred_common_name: String?,
    val iconic_taxon_name: String?,
    val rank: String?,
    val ancestor_ids: List<Int?>,
    val wikipedia_url: String?,
    val extinct: Boolean?,
    val universal_search_rank: Int?,
    val default_photo: DefaultPhotoEntity?,
    val wikipedia_summary: String? = null,
)

data class DefaultPhotoEntity(
    val url : String?,
    val square_url: String?,
    val medium_url: String?,
    val attribution: String?,
)

//functions to convert from entities to domain
fun ObservationEntity.toObservation(): Observation {
    return Observation(
        id = id,
        uuid = uuid,
        created_at = created_at,
        location = location,
        spam = spam,
        identifications = identifications.map { it?.toIdentification() },
        photos = photos.map { it?.toPhoto() }
    )
}

fun PhotoEntity.toPhoto(): Photo {
    return Photo(
        id = id,
        url = url,
        attribution = attribution,
        license_code = license_code
    )
}

fun IdentificationEntity.toIdentification(): Identification {
    return Identification(
        id = id,
        uuid = uuid,
        created_at = created_at,
        current = current,
        taxon = taxon?.toTaxon()
    )
}

fun TaxonEntity.toTaxon(): Taxon {
    return Taxon(
        id = id,
        uuid = uuid,
        name = name,
        preferred_common_name = preferred_common_name,
        iconic_taxon_name = iconic_taxon_name,
        rank = rank,
        ancestor_ids = ancestor_ids,
        wikipedia_url = wikipedia_url,
        extinct = extinct,
        universal_search_rank = universal_search_rank,
        default_photo = default_photo?.toDefaultPhoto(),
        wikipedia_summary = wikipedia_summary
    )
}

fun DefaultPhotoEntity.toDefaultPhoto(): DefaultPhoto {
    return DefaultPhoto(
        url = url,
        square_url = square_url,
        medium_url = medium_url,
        attribution = attribution
    )
}

//List<Observation> to List<ObservationEntity>
fun List<ObservationEntity>.toObservationList(): List<Observation> {
    return map { it.toObservation() }
}

//List<ObservationEntity> to List<Observation>
fun List<Observation>.toObservationEntityList(): List<ObservationEntity> {
    return map { it.toObservationEntity() }
}

//Observation to ObservationEntity
fun Observation.toObservationEntity(): ObservationEntity {
    return ObservationEntity(
        id = id,
        uuid = uuid,
        created_at = created_at,
        location = location,
        spam = spam,
        identifications = identifications.map { it?.toIdentificationEntity() },
        photos = photos.map { it?.toPhotoEntity() },
        taxon = taxon?.toTaxonEntity()
    )
}

//Photo to PhotoEntity
fun Photo.toPhotoEntity(): PhotoEntity {
    return PhotoEntity(
        id = id,
        url = url,
        attribution = attribution,
        license_code = license_code
    )
}

//Identification to IdentificationEntity
fun Identification.toIdentificationEntity(): IdentificationEntity {
    return IdentificationEntity(
        id = id,
        uuid = uuid,
        created_at = created_at,
        current = current,
        taxon = taxon?.toTaxonEntity()
    )
}

//Taxon to TaxonEntity
fun Taxon.toTaxonEntity(): TaxonEntity {
    return TaxonEntity(
        id = id,
        uuid = uuid,
        name = name,
        preferred_common_name = preferred_common_name,
        iconic_taxon_name = iconic_taxon_name,
        rank = rank,
        ancestor_ids = ancestor_ids,
        wikipedia_url = wikipedia_url,
        extinct = extinct,
        universal_search_rank = universal_search_rank,
        default_photo = default_photo?.toDefaultPhotoEntity()
    )
}

//DefaultPhoto to DefaultPhotoEntity
fun DefaultPhoto.toDefaultPhotoEntity(): DefaultPhotoEntity {
    return DefaultPhotoEntity(
        url = url,
        square_url = square_url,
        medium_url = medium_url,
        attribution = attribution
    )
}

