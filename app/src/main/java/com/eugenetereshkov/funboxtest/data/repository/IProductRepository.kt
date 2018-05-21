package com.eugenetereshkov.funboxtest.data.repository

import com.eugenetereshkov.funboxtest.data.entity.Product
import com.eugenetereshkov.funboxtest.data.storage.RawAppData
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface IProductRepository {
    fun getProducts(): Single<List<Product>>
    fun saveProducts(data: List<Product>): Completable
}

class ProductRepository(
        private val rawAppData: RawAppData
) : IProductRepository {

    override fun getProducts(): Single<List<Product>> =
            rawAppData.getData()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

    override fun saveProducts(data: List<Product>): Completable =
            rawAppData.saveData(data)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
}