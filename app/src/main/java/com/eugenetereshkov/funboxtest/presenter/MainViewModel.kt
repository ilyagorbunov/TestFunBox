package com.eugenetereshkov.funboxtest.presenter

import android.arch.lifecycle.ViewModel
import ru.terrakok.cicerone.Router

class MainViewModel(
        private val router: Router
) : ViewModel() {

    fun onBackPressed() {
        router.exit()
    }
}