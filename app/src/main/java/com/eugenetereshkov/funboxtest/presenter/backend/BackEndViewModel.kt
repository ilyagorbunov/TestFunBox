package com.eugenetereshkov.funboxtest.presenter.backend

import android.arch.lifecycle.ViewModel
import com.eugenetereshkov.funboxtest.Screen
import com.eugenetereshkov.funboxtest.data.entity.Product
import ru.terrakok.cicerone.Router

class BackEndViewModel(
        private val router: Router
) : ViewModel() {

    fun onAddProductPressed() {
        router.navigateTo(Screen.EDIT_PRODUCT_SCREEN)
    }

    fun onEditProductPressed(product: Product) {
        router.navigateTo(Screen.EDIT_PRODUCT_SCREEN, product)
    }
}