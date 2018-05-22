package com.eugenetereshkov.funboxtest.presenter.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.eugenetereshkov.funboxtest.data.entity.Product
import com.eugenetereshkov.funboxtest.data.repository.IProductRepository
import com.eugenetereshkov.funboxtest.extension.bindTo
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router

class MainViewModel(
        private val router: Router,
        private val productRepository: IProductRepository
) : ViewModel() {

    val dataLiveData = MutableLiveData<List<Product>>()
    val loadingLiveData = MutableLiveData<Boolean>()
    var data: MutableList<Product> = mutableListOf()

    private val disposable = CompositeDisposable()

    init {
        productRepository.getProducts()
                .doOnSubscribe { loadingLiveData.postValue(true) }
                .doAfterTerminate { loadingLiveData.postValue(false) }
                .subscribe(
                        { products: List<Product> ->
                            data = products as MutableList<Product>
                            dataLiveData.value = data.copy()
                        },
                        { router.showSystemMessage(it.toString()) }
                )
                .bindTo(disposable)

    }

    private fun MutableList<Product>.copy(): List<Product> {
        val result = mutableListOf<Product>()
        this.forEach { result.add(it.copy()) }
        return result
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }

    fun onDataChanged(index: Int, product: Product) {
        data[index] = product
        saveData()
    }

    fun onDataAdded(newProduct: Product) {
        data.add(newProduct)
        saveData()
    }

    fun byeProduct(product: Product) {
        data.firstOrNull { it == product }?.run {
            if (count > 0) {
                count--
                saveData()
            }
        }
    }

    fun onBackPressed() {
        router.exit()
    }

    private fun saveData() {
        productRepository.saveProducts(data)
                .doOnSubscribe { loadingLiveData.postValue(true) }
                .doAfterTerminate { loadingLiveData.postValue(false) }
                .subscribe {
                    dataLiveData.value = data.copy()
                    router.showSystemMessage("Saved")
                }
                .bindTo(disposable)
    }
}