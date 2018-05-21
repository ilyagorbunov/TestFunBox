package com.eugenetereshkov.funboxtest.presenter.backend

import android.arch.lifecycle.ViewModel
import com.eugenetereshkov.funboxtest.Screen
import ru.terrakok.cicerone.Router

class BackEndViewModel(
        private val router: Router
) : ViewModel() {

    fun onAddProductPressed() {
        router.navigateTo(Screen.EDIT_PRODUCT_SCREEN)
    }

    fun onEditProductPressed(index: Int) {
        router.navigateTo(Screen.EDIT_PRODUCT_SCREEN, index)
    }
}