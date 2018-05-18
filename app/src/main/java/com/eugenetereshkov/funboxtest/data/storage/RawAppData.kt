package com.eugenetereshkov.funboxtest.data.storage

import android.content.res.AssetManager
import com.eugenetereshkov.funboxtest.CSVFile
import com.eugenetereshkov.funboxtest.data.entity.Product
import io.reactivex.Single


class RawAppData(
        private val assets: AssetManager
) {
    fun getProducts(): Single<List<Product>> = fromAsset("data.csv")

    private fun fromAsset(pathToAsset: String): Single<List<Product>> = Single.defer {
        assets.open(pathToAsset).bufferedReader().useLines { lines ->
            val result = lines
                    .map {
                        it.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                                .map { it.replace("\"", "") }
                                .toTypedArray()
                    }
                    .toList()
                    .run { CSVFile.convertToProduct(this) }
            Single.just<List<Product>>(result)
        }
    }
}
