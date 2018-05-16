package com.eugenetereshkov.funboxtest.ui.storefront


import com.eugenetereshkov.funboxtest.R
import com.eugenetereshkov.funboxtest.ui.common.BaseFragment


class StoreFrontFragment : BaseFragment() {

    companion object {
        fun newInstance() = StoreFrontFragment()
    }

    override val layoutResId: Int = R.layout.fragment_store_front
}
