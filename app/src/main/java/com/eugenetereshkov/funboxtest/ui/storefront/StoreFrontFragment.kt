package com.eugenetereshkov.funboxtest.ui.storefront


import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import androidx.core.widget.toast
import com.eugenetereshkov.funboxtest.R
import com.eugenetereshkov.funboxtest.data.entity.Product
import com.eugenetereshkov.funboxtest.presenter.main.MainViewModel
import com.eugenetereshkov.funboxtest.ui.common.BaseFragment
import com.eugenetereshkov.funboxtest.ui.common.list.StoreFrontAdapter
import kotlinx.android.synthetic.main.fragment_store_front.*
import org.koin.android.architecture.ext.sharedViewModel


class StoreFrontFragment : BaseFragment() {

    companion object {
        fun newInstance() = StoreFrontFragment()
    }

    override val layoutResId: Int = R.layout.fragment_store_front

    private val adapter by lazy { StoreFrontAdapter() }
    private val viewModel: MainViewModel by sharedViewModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.title = getString(R.string.store_front)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = this@StoreFrontFragment.adapter
        }.also {
            LinearSnapHelper().attachToRecyclerView(it)
        }
        adapter.submitList((0..10).map { Product("Samsung", 23, 1) }.toMutableList())

        viewModel.intervalLiveData.observe(this, Observer { interval ->
            interval?.let { context?.toast(it.toString()) }
        })
    }
}
