package com.eugenetereshkov.funboxtest.ui.storefront


import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import com.eugenetereshkov.funboxtest.R
import com.eugenetereshkov.funboxtest.data.entity.Product
import com.eugenetereshkov.funboxtest.ui.common.BaseFragment
import com.eugenetereshkov.funboxtest.ui.common.list.StoreFrontAdapter
import kotlinx.android.synthetic.main.fragment_store_front.*


class StoreFrontFragment : BaseFragment() {

    companion object {
        fun newInstance() = StoreFrontFragment()
    }

    override val layoutResId: Int = R.layout.fragment_store_front

    private val adapter by lazy { StoreFrontAdapter() }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.title = getString(R.string.store_front)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = this@StoreFrontFragment.adapter
        }
        LinearSnapHelper().attachToRecyclerView(recyclerView)

        adapter.submitList((0..10).map { Product("Samsung", 23, 1) }.toMutableList())
    }
}
