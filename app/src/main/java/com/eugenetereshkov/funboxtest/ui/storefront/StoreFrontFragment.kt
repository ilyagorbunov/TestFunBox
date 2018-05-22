package com.eugenetereshkov.funboxtest.ui.storefront


import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import com.eugenetereshkov.funboxtest.R
import com.eugenetereshkov.funboxtest.presenter.main.MainViewModel
import com.eugenetereshkov.funboxtest.presenter.storefront.StoreFrontViewModel
import com.eugenetereshkov.funboxtest.ui.common.BaseFragment
import com.eugenetereshkov.funboxtest.ui.common.list.StoreFrontAdapter
import kotlinx.android.synthetic.main.fragment_store_front.*
import org.koin.android.architecture.ext.sharedViewModel
import org.koin.android.architecture.ext.viewModel


class StoreFrontFragment : BaseFragment() {

    companion object {
        fun newInstance() = StoreFrontFragment()
    }

    override val layoutResId: Int = R.layout.fragment_store_front

    private val adapter by lazy { StoreFrontAdapter { mainViewModel.byeProduct(it) } }
    private val mainViewModel: MainViewModel by sharedViewModel()
    private val viewModel: StoreFrontViewModel by viewModel()

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

        mainViewModel.dataLiveData.observe(this, Observer { data ->
            data?.let { adapter.setData(viewModel.processData(it)) }
        })
    }

    override fun onBackPressed() {
        viewModel.onBackPressed()
    }
}
