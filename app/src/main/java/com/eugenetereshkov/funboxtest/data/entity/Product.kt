package com.eugenetereshkov.funboxtest.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Product(
        var name: String,
        var price: Int,
        var count: Int
) : Parcelable
