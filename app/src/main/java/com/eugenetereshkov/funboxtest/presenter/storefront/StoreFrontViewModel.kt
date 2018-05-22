package com.eugenetereshkov.funboxtest.presenter.storefront

import android.arch.lifecycle.ViewModel
import com.eugenetereshkov.funboxtest.data.entity.Product
import com.eugenetereshkov.funboxtest.ui.common.list.StoreFrontAdapter
import ru.terrakok.cicerone.Router

class StoreFrontViewModel(
        private val router: Router
) : ViewModel() {

    fun processData(data: List<Product>): List<StoreFrontAdapter.LoadProduct> =
            data.filter { it.count > 0 }.map { StoreFrontAdapter.LoadProduct(product = it) }

    fun onBackPressed() = router.exit()
}