package com.eugenetereshkov.funboxtest.presenter.editproduct

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.eugenetereshkov.funboxtest.data.entity.Product
import ru.terrakok.cicerone.Router

class EditProductViewModel(
        private val router: Router
) : ViewModel() {

    val productChangesLiveData = MutableLiveData<Boolean>()

    private var product: Product? = null

    fun onBackPressed() {
        router.exit()
    }

    fun onProductChanged(data: Pair<Product, Boolean>, isEditMode: Boolean) {
        val product = data.first
        when {
            data.second.not() -> productChangesLiveData.value = false
            this.product != null -> productChangesLiveData.value = this.product.toString() != product.toString()
            else -> {
                if (isEditMode.not()) {
                    productChangesLiveData.value = true
                    this.product = Product("", "", "")
                } else {
                    this.product = product
                }
            }
        }
    }
}