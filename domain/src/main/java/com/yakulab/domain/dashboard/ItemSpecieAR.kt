package com.yakulab.domain.dashboard

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class ItemSpecieAR (
    val uuid: String?,
    val urlModel: String?,
    val name: String?,
    val preview: String?,
    val scaleInUnit: Float = 0.5f,
)


@Parcelize
data class ItemSpecieARParcelable (
    val uuid: String?,
    val urlModel: String?,
    val name: String?,
    val preview: String?,
    val scaleInUnit: Float = 0.5f,
): Parcelable


fun ItemSpecieAR.toParcelable(): ItemSpecieARParcelable {
    return ItemSpecieARParcelable(
        uuid = uuid,
        urlModel = urlModel,
        name = name,
        preview = preview,
        scaleInUnit = scaleInUnit
    )
}

fun ItemSpecieARParcelable.toItemSpecieAR(): ItemSpecieAR {
    return ItemSpecieAR(
        uuid = uuid,
        urlModel = urlModel,
        name = name,
        preview = preview,
        scaleInUnit = scaleInUnit
    )
}