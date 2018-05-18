package com.eugenetereshkov.funboxtest.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Product(
        var name: String,
        var price: String,
        var count: String
) : Parcelable
