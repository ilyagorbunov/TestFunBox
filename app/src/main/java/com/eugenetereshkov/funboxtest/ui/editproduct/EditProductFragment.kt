package com.eugenetereshkov.funboxtest.ui.editproduct


import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.MenuItem
import androidx.core.os.bundleOf
import com.eugenetereshkov.funboxtest.R
import com.eugenetereshkov.funboxtest.data.entity.Product
import com.eugenetereshkov.funboxtest.extension.bindTo
import com.eugenetereshkov.funboxtest.presenter.editproduct.EditProductViewModel
import com.eugenetereshkov.funboxtest.ui.common.BaseFragment
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.fragment_edit_product.*
import org.koin.android.ext.android.inject


typealias EditProduct = BiFunction<Pair<String, Boolean>, Pair<Int, Boolean>, Pair<Product, Boolean>>

class EditProductFragment : BaseFragment() {

    companion object {
        const val PRODUCT = "product"

        fun newInstance(data: Product?) = EditProductFragment().apply {
            data?.let { arguments = bundleOf(PRODUCT to it) }
        }
    }

    override val layoutResId: Int = R.layout.fragment_edit_product

    private val viewModel: EditProductViewModel by inject()
    private var isEditMode: Boolean = false
    private val disposable = CompositeDisposable()
    private val saveMenuItem by lazy { toolbar.menu.findItem(R.id.menu_save) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val data = arguments?.let {
            isEditMode = true
            it.getParcelable<Product>(PRODUCT)
        }

        toolbar.apply {
            title = if (isEditMode) getString(R.string.editing_product) else getString(R.string.adding_product)
            inflateMenu(R.menu.edit_backend_menu)
            saveMenuItem.isVisible = false
            setOnMenuItemClickListener { item: MenuItem ->
                when (item.itemId) {
                    R.id.menu_save -> {
                        return@setOnMenuItemClickListener true
                    }
                }
                false
            }
            setNavigationOnClickListener { viewModel.onBackPressed() }
        }

        viewModel.productChangesLiveData.observe(this, Observer { changed ->
            changed?.let { saveMenuItem.isVisible = it }
        })

        val nameObservable = textViewName.textChanges()
                .map { it.trim().toString() }
                .map { Pair(it, it.isNotEmpty()) }

        val priceObservable = editTextPrice.textChanges()
                .map { it.trim().toString() }
                .map { if (it.isNotEmpty()) Pair(it.toInt(), true) else Pair(0, false) }

        Observable.combineLatest(
                nameObservable,
                priceObservable,
                EditProduct { namePair, pricePair ->
                    Pair(Product(namePair.first, pricePair.first, 2), namePair.second && pricePair.second)
                })
                .subscribe { viewModel.onProductChanged(it, isEditMode) }
                .bindTo(disposable)


        data?.run {
            textViewName.setText(name)
            editTextPrice.setText(price.toString())
        }
    }

    override fun onStop() {
        disposable.clear()
        super.onStop()
    }
}
