package com.eugenetereshkov.funboxtest.presenter.editproduct

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.eugenetereshkov.funboxtest.data.entity.Product
import com.eugenetereshkov.funboxtest.ui.editproduct.EditProductFragment
import ru.terrakok.cicerone.Router
import timber.log.Timber

class EditProductViewModel(
        private val router: Router,
        val idProduct: Int
) : ViewModel() {

    val productChangesLiveData = MutableLiveData<Boolean>()
    val productLiveData = MutableLiveData<Product>()
    var isEditMode: Boolean = idProduct != EditProductFragment.NO_PRODUCT

    var oldProduct = Product()
        set(value) {
            field = value
            productLiveData.value = value
        }
    lateinit var newProduct: Product

    init {
        productLiveData.value = oldProduct
    }

    override fun onCleared() {
        super.onCleared()
        Timber.d("onCleared")
    }

    fun onProductChanged(data: Triple<String, String, String>) {
        val (name, price, count) = data
        val isValidData = name.isNotEmpty() && price.isNotEmpty() && count.isNotEmpty()

        if (isValidData.not()) {
            productChangesLiveData.value = false
            return
        }

        newProduct = Product(name, price.toFloat(), count.toInt())
        productChangesLiveData.value = oldProduct.toString() != newProduct.toString()
    }

    fun onBackPressed() {
        router.exit()
    }
}