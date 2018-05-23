package com.eugenetereshkov.funboxtest.data.entity

import com.eugenetereshkov.funboxtest.data.storage.IStorageFormatVisitor
import io.reactivex.Completable
import io.reactivex.Single


sealed class StorageFile(val name: String) {
    abstract fun read(visitor: IStorageFormatVisitor): Single<List<Product>>
    abstract fun write(visitor: IStorageFormatVisitor, data: List<Product>): Completable
}

class CSVStorageFile(name: String) : StorageFile(name) {

    override fun read(visitor: IStorageFormatVisitor): Single<List<Product>> = visitor.read(this)

    override fun write(visitor: IStorageFormatVisitor, data: List<Product>): Completable = visitor.write(this, data)

    fun convertToProduct(data: List<Array<String>>): List<Product> =
            data.map { Product(it[0], it[1].toFloat(), it[2].toInt()) }.toList()
}
