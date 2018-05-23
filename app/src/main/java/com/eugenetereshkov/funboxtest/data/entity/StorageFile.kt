package com.eugenetereshkov.funboxtest.data.entity

import com.eugenetereshkov.funboxtest.data.storage.IStorageFormatVisitor


sealed class StorageFile(val name: String) {
    abstract fun read(visitor: IStorageFormatVisitor)
    abstract fun write(visitor: IStorageFormatVisitor, data: List<Product>)
}

class CSVStorageFile(name: String) : StorageFile(name) {

    override fun read(visitor: IStorageFormatVisitor) {
        visitor.read(this)
    }

    override fun write(visitor: IStorageFormatVisitor, data: List<Product>) {
        visitor.write(this, data)
    }

    fun convertToProduct(data: List<Array<String>>): List<Product> =
            data.map { Product(it[0], it[1].toFloat(), it[2].toInt()) }.toList()
}
