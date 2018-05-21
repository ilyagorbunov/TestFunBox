package com.eugenetereshkov.funboxtest.data.storage

interface IStorageFormat {
    fun parse(visitor: IParseStorageFormatVisitor)
}

class CSVStorageFormat : IStorageFormat {
    override fun parse(visitor: IParseStorageFormatVisitor) {

    }
}