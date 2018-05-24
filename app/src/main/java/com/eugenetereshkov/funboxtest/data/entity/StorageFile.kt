package com.eugenetereshkov.funboxtest.data.entity

import com.eugenetereshkov.funboxtest.data.storage.IStorageFormatVisitor
import io.reactivex.Completable
import io.reactivex.Single


interface StorageFile {
    val name: String
    fun read(visitor: IStorageFormatVisitor): Single<List<Product>>
    fun write(visitor: IStorageFormatVisitor, data: List<Product>): Completable
}

class CSVStorageFile(override val name: String) : StorageFile {

    override fun read(visitor: IStorageFormatVisitor): Single<List<Product>> = visitor.read(this)

    override fun write(visitor: IStorageFormatVisitor, data: List<Product>): Completable = visitor.write(this, data)

    fun convertToProduct(data: List<Array<String>>): List<Product> =
            data.map { Product(it[0], it[1].toFloat(), it[2].toInt()) }.toList()
}
