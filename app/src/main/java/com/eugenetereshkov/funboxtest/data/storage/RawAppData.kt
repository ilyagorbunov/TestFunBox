package com.eugenetereshkov.funboxtest.data.storage

import android.content.res.AssetManager
import com.eugenetereshkov.funboxtest.CSVFile
import com.eugenetereshkov.funboxtest.data.entity.Product
import com.opencsv.CSVParser
import com.opencsv.CSVWriter
import io.reactivex.Completable
import io.reactivex.Single
import java.io.File
import java.io.FileWriter


class RawAppData(
        private val assets: AssetManager,
        private val filesDir: File
) {

    private companion object {
        private const val DEFAULT_FILE_NAME = "data.csv"
    }

    private fun fromAsset(pathToAsset: String): Single<List<Product>> = Single.defer {
        assets.open(pathToAsset).bufferedReader().useLines { lines ->
            val result = lines
                    .map { CSVParser().parseLine(it) }
                    .toList()
                    .run { CSVFile.convertToProduct(this) }
            Single.just<List<Product>>(result)
        }
    }

    fun getData(): Single<List<Product>> = Single.defer {
        val file = File(filesDir, "data.csv")

        if (file.exists().not()) {
            file.createNewFile()
            assets.open(DEFAULT_FILE_NAME).use { inputStream ->
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }

        file.inputStream().bufferedReader().useLines { lines ->
            val result = lines
                    .map { CSVParser().parseLine(it) }
                    .toList()
                    .run { CSVFile.convertToProduct(this) }
            Single.just<List<Product>>(result)
        }
    }

    fun saveData(data: List<Product>): Completable = Completable.defer {
        CSVWriter(FileWriter("${filesDir.absolutePath}/$DEFAULT_FILE_NAME")).use { writer ->
            data.forEach { product ->
                product.toStringForConverts()
                        .split("#".toRegex())
                        .dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                        .run { writer.writeNext(this) }
            }
        }
        Completable.complete()
    }
}
