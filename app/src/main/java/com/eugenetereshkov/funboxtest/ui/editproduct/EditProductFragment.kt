package com.eugenetereshkov.funboxtest.ui.editproduct


import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import com.eugenetereshkov.funboxtest.R
import com.eugenetereshkov.funboxtest.extension.bindTo
import com.eugenetereshkov.funboxtest.presenter.editproduct.EditProductViewModel
import com.eugenetereshkov.funboxtest.presenter.main.MainViewModel
import com.eugenetereshkov.funboxtest.ui.common.BaseFragment
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function3
import kotlinx.android.synthetic.main.fragment_edit_product.*
import org.koin.android.architecture.ext.sharedViewModel
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit


typealias EditProduct = Function3<String, String, String, Triple<String, String, String>>

class EditProductFragment : BaseFragment() {

    companion object {
        const val PRODUCT_ID = "product_index"
        const val NO_PRODUCT = -1

        fun newInstance(data: Int?) = EditProductFragment().apply {
            arguments = bundleOf(PRODUCT_ID to (data ?: NO_PRODUCT))
        }
    }

    override val layoutResId: Int = R.layout.fragment_edit_product

    private val mainViewModel: MainViewModel by sharedViewModel()
    private val viewModel: EditProductViewModel by inject { mapOf(PRODUCT_ID to idProduct) }
    private val idProduct get() = arguments?.getInt(PRODUCT_ID) ?: NO_PRODUCT
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

        toolbar.apply {
            title = if (viewModel.isEditMode) getString(R.string.editing_product) else getString(R.string.adding_product)
            inflateMenu(R.menu.edit_backend_menu)
            saveMenuItem.isVisible = false
            setOnMenuItemClickListener { item: MenuItem ->
                when (item.itemId) {
                    R.id.menu_save -> {
                        mainViewModel.onSavePressed(viewModel.idProduct, viewModel.newProduct, viewModel.isEditMode)
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

        if (viewModel.isEditMode) viewModel.oldProduct = mainViewModel.data[viewModel.idProduct]

        viewModel.productChangesLiveData.observe(this, Observer { changed ->
            changed?.let { saveMenuItem.isVisible = it }
        })

        viewModel.productLiveData.observe(this, Observer { product ->
            product?.run {
                textViewName.setText(name)
                editTextPrice.setText(price.toString())
                editTextCount.setText(count.toString())
            }
        })

        val nameObservable = textViewName.textChanges()
                .map { it.trim().toString() }

        val priceObservable = editTextPrice.textChanges()
                .map { it.trim().toString() }

        val countObservable = editTextCount.textChanges()
                .map { it.trim().toString() }

        Observable.combineLatest(
                nameObservable,
                priceObservable,
                countObservable,
                EditProduct { name, price, count -> Triple(name, price, count) })
                .debounce(250, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { viewModel.onProductChanged(it) }
                .bindTo(disposable)
    }

    override fun onStop() {
        disposable.clear()
        super.onStop()
    }
}
