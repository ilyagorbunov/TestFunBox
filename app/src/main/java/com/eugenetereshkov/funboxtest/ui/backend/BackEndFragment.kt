package com.eugenetereshkov.funboxtest.ui.backend


import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.eugenetereshkov.funboxtest.R
import com.eugenetereshkov.funboxtest.data.entity.Product
import com.eugenetereshkov.funboxtest.presenter.backend.BackEndViewModel
import com.eugenetereshkov.funboxtest.presenter.main.MainViewModel
import com.eugenetereshkov.funboxtest.ui.common.BaseFragment
import com.eugenetereshkov.funboxtest.ui.common.list.BackEndProductAdapter
import kotlinx.android.synthetic.main.fragment_back_end.*
import org.koin.android.architecture.ext.sharedViewModel
import org.koin.android.architecture.ext.viewModel


class BackEndFragment : BaseFragment() {

    companion object {
        fun newInstance() = BackEndFragment()
    }

    override val layoutResId: Int = R.layout.fragment_back_end

    private val adapter by lazy {
        BackEndProductAdapter({ backEndViewModel.onEditProductPressed(it) })
    }
    private val mainViewModel: MainViewModel by sharedViewModel()
    private val backEndViewModel: BackEndViewModel by viewModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.apply {
            title = getString(R.string.back_end)
            inflateMenu(R.menu.backend_menu)
            setOnMenuItemClickListener { item: MenuItem ->
                when (item.itemId) {
                    R.id.menu_back_end -> {
                        backEndViewModel.onAddProductPressed()
                        return@setOnMenuItemClickListener true
                    }
                }
                false
            }
        }


        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@BackEndFragment.adapter
        }

        adapter.submitList((0..10).map { Product("Samsung", 23, 1) }.toMutableList())
    }
}
