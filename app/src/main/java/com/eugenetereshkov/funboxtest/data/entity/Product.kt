package com.eugenetereshkov.funboxtest.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Product(
        var name: String = "",
        var price: Float = 0F,
        var count: Int = 0
) : Parcelable {

    companion object {
        const val PRICE = "price"
        const val COUNT = "count"
        const val NAME = "name"
        const val LOADING = "loading"
    }

    fun toStringForConverts(): String {
        return "$name#$price#$count"
    }
}
