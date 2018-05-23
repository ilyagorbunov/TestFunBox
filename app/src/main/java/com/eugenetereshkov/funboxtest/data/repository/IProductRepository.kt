package com.eugenetereshkov.funboxtest.data.repository

import com.eugenetereshkov.funboxtest.data.entity.CSVStorageFile
import com.eugenetereshkov.funboxtest.data.entity.Product
import com.eugenetereshkov.funboxtest.data.storage.IStorageFormatVisitor
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface IProductRepository {
    fun getProducts(): Single<List<Product>>
    fun saveProducts(data: List<Product>): Completable
}

class ProductRepository(
        private val storageFormatVisitor: IStorageFormatVisitor
) : IProductRepository {

    override fun getProducts(): Single<List<Product>> =
            CSVStorageFile("data.csv").read(storageFormatVisitor)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

    override fun saveProducts(data: List<Product>): Completable =
            CSVStorageFile("data.csv").write(storageFormatVisitor, data)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
}