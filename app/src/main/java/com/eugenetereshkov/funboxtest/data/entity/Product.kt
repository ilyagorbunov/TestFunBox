package com.eugenetereshkov.funboxtest.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Product(
        var name: String,
        var price: Float,
        var count: Int
) : Parcelable {

    fun toStringForConverts(): String {
        return "$name#$price#$count"
    }
}
