package com.eugenetereshkov.funboxtest.ui.backend


import com.eugenetereshkov.funboxtest.R
import com.eugenetereshkov.funboxtest.ui.common.BaseFragment


class BackEndFragment : BaseFragment() {

    companion object {
        fun newInstance() = BackEndFragment()
    }

    override val layoutResId: Int = R.layout.fragment_back_end
}
