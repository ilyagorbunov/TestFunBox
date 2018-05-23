package com.eugenetereshkov.funboxtest.data.storage

import android.content.res.AssetManager
import com.eugenetereshkov.funboxtest.data.entity.CSVStorageFile
import com.eugenetereshkov.funboxtest.data.entity.Product
import com.opencsv.CSVParser
import com.opencsv.CSVWriter
import io.reactivex.Completable
import io.reactivex.Single
import java.io.File
import java.io.FileWriter

interface IStorageFormatVisitor {
    fun read(file: CSVStorageFile): Single<List<Product>>
    fun write(file: CSVStorageFile, data: List<Product>): Completable
}

class StorageFormatVisitor(
        private val assets: AssetManager,
        private val filesDir: File
) : IStorageFormatVisitor {

    override fun read(file: CSVStorageFile): Single<List<Product>> = Single.defer {
        val extFile = File(filesDir, file.name)

        if (extFile.exists().not()) {
            extFile.createNewFile()
            assets.open(file.name).use { inputStream ->
                extFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }

        extFile.inputStream().bufferedReader().useLines { lines ->
            val result = lines
                    .map { CSVParser().parseLine(it) }
                    .toList()
                    .run { file.convertToProduct(this) }
            Single.just<List<Product>>(result)
        }
    }

    override fun write(file: CSVStorageFile, data: List<Product>): Completable = Completable.defer {
        CSVWriter(FileWriter("${filesDir.absolutePath}/${file.name}")).use { writer ->
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