package com.eugenetereshkov.funboxtest.ui.editproduct


import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import com.eugenetereshkov.funboxtest.R
import com.eugenetereshkov.funboxtest.data.entity.Product
import com.eugenetereshkov.funboxtest.extension.bindTo
import com.eugenetereshkov.funboxtest.presenter.editproduct.EditProductViewModel
import com.eugenetereshkov.funboxtest.presenter.main.MainViewModel
import com.eugenetereshkov.funboxtest.ui.common.BaseFragment
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function3
import kotlinx.android.synthetic.main.fragment_edit_product.*
import org.koin.android.architecture.ext.sharedViewModel
import org.koin.android.ext.android.inject


typealias EditProduct = Function3<Pair<String, Boolean>, Pair<Float, Boolean>, Pair<Int, Boolean>, Pair<Product, Boolean>>

class EditProductFragment : BaseFragment() {

    companion object {
        const val PRODUCT_INDEX = "product_index"

        fun newInstance(data: Int?) = EditProductFragment().apply {
            data?.let { arguments = bundleOf(PRODUCT_INDEX to it) }
        }
    }

    override val layoutResId: Int = R.layout.fragment_edit_product

    private val mainViewModel: MainViewModel by sharedViewModel()
    private val viewModel: EditProductViewModel by inject()
    private var isEditMode: Boolean = false
    private val disposable = CompositeDisposable()
    private val saveMenuItem by lazy { toolbar.menu.findItem(R.id.menu_save) }
    private val clickListener by lazy {
        View.OnClickListener { view ->
            when (view.id) {
                R.id.imageButtonAdd -> {
                    editTextCount.run { setText(text.toString().toInt().inc().toString()) }
                }
                R.id.imageButtonRemove -> {
                    val count = editTextCount.text.toString().toInt()
                    if (count > 0) editTextCount.setText(count.dec().toString())
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val index = arguments?.let {
            isEditMode = true
            it.getInt(PRODUCT_INDEX)
        }

        toolbar.apply {
            title = if (isEditMode) getString(R.string.editing_product) else getString(R.string.adding_product)
            inflateMenu(R.menu.edit_backend_menu)
            saveMenuItem.isVisible = false
            setOnMenuItemClickListener { item: MenuItem ->
                when (item.itemId) {
                    R.id.menu_save -> {
                        if (isEditMode) {
                            mainViewModel.onDataChanged(index ?: -1, viewModel.newProduct)
                        } else {
                            mainViewModel.onDataAdded(viewModel.newProduct)
                        }
                        viewModel.onBackPressed()
                        return@setOnMenuItemClickListener true
                    }
                }
                false
            }
            setNavigationOnClickListener { viewModel.onBackPressed() }
        }

        imageButtonAdd.setOnClickListener(clickListener)
        imageButtonRemove.setOnClickListener(clickListener)

        viewModel.productChangesLiveData.observe(this, Observer { changed ->
            changed?.let { saveMenuItem.isVisible = it }
        })

        val nameObservable = textViewName.textChanges()
                .map { it.trim().toString() }
                .map { Pair(it, it.isNotEmpty()) }

        val priceObservable = editTextPrice.textChanges()
                .map { it.trim().toString() }
                .map { if (it.isNotEmpty()) Pair(it.toFloat(), true) else Pair(0f, false) }

        val countObservable = editTextCount.textChanges()
                .map { it.trim().toString() }
                .map { if (it.isNotEmpty()) Pair(it.toInt(), true) else Pair(0, false) }

        Observable.combineLatest(
                nameObservable,
                priceObservable,
                countObservable,
                EditProduct { namePair, pricePair, countPair ->
                    val product = Product(
                            name = namePair.first,
                            price = pricePair.first,
                            count = countPair.first
                    )
                    val isValid = namePair.second && pricePair.second && countPair.second
                    Pair(product, isValid)
                })
                .subscribe { viewModel.onProductChanged(it, isEditMode) }
                .bindTo(disposable)

        val (name, price, count) = index?.let {
            mainViewModel.data[it].run { Triple(name, price, count) }
        } ?: Triple("", 0f, 0)

        textViewName.setText(name)
        editTextPrice.setText(price.toString())
        editTextCount.setText(count.toString())
    }

    override fun onStop() {
        disposable.clear()
        super.onStop()
    }
}
