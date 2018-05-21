package com.eugenetereshkov.funboxtest.presenter.editproduct

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.eugenetereshkov.funboxtest.data.entity.Product
import ru.terrakok.cicerone.Router

class EditProductViewModel(
        private val router: Router
) : ViewModel() {

    val productChangesLiveData = MutableLiveData<Boolean>()

    private var oldProduct: Product? = null
    lateinit var newProduct: Product

    fun onBackPressed() {
        router.exit()
    }

    fun onProductChanged(data: Pair<Product, Boolean>, isEditMode: Boolean) {
        val product = data.first
        newProduct = product

        when {
            data.second.not() -> productChangesLiveData.value = false
            this.oldProduct != null -> productChangesLiveData.value = this.oldProduct.toString() != product.toString()
            else -> {
                if (isEditMode.not()) {
                    productChangesLiveData.value = true
                    this.oldProduct = Product("", 0f, 0)
                } else {
                    this.oldProduct = product
                }
            }
        }
    }
}