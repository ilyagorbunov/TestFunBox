package com.eugenetereshkov.funboxtest.ui.common.list

import android.os.Bundle
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.eugenetereshkov.funboxtest.R
import com.eugenetereshkov.funboxtest.data.entity.Product
import com.eugenetereshkov.funboxtest.extension.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_product.*

class StoreFrontAdapter(
        private val listener: (product: Product) -> Unit
) : ListAdapter<StoreFrontAdapter.LoadProduct, StoreFrontAdapter.ViewHolder>(ProductDiffUtilsCallBack) {

    private object ProductDiffUtilsCallBack : DiffUtil.ItemCallback<LoadProduct>() {
        override fun areItemsTheSame(oldItem: LoadProduct, newItem: LoadProduct): Boolean =
                oldItem.product.name == newItem.product.name

        override fun areContentsTheSame(oldItem: LoadProduct, newItem: LoadProduct): Boolean =
                oldItem.product.count == newItem.product.count && oldItem.product.price == newItem.product.price
                        && oldItem.loading == newItem.loading

        override fun getChangePayload(oldItem: LoadProduct, newItem: LoadProduct): Any = Bundle().apply {
            if (oldItem.product.price != newItem.product.price) putFloat(Product.PRICE, newItem.product.price)
            if (oldItem.product.count != newItem.product.count) putInt(Product.COUNT, newItem.product.count)
            if (oldItem.loading != newItem.loading) putBoolean(Product.LOADING, newItem.loading)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_product)) { position ->
            val item = getItem(position)
            item.loading = true
            listener(item.product)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
            return
        }

        (payloads[0] as Bundle).keySet().forEach { key ->
            val item = getItem(position)
            if (key == Product.PRICE) holder.bindPrice(item.product.price)
            if (key == Product.COUNT) holder.bindCount(item.product.count)
            if (key == Product.LOADING) holder.bindLoading(item.loading)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, getItem(position))
    }

    fun setData(data: List<LoadProduct>) {
        submitList(data)
    }

    class ViewHolder(
            override val containerView: View,
            listener: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private var index = RecyclerView.NO_POSITION

        init {
            buttonBye.setOnClickListener {
                bindLoading(true)
                listener(index)
            }
        }

        fun bind(position: Int, item: LoadProduct) {
            index = position
            textViewName.text = item.product.name
            bindLoading(item.loading)
            bindCount(item.product.count)
            bindPrice(item.product.price)
        }

        fun bindPrice(price: Float) {
            textViewPrice.run { text = context.getString(R.string.price_format, price) }
        }

        fun bindCount(count: Int) {
            textViewCount.run { text = context.getString(R.string.count_format, count) }
        }

        fun bindLoading(loading: Boolean) {
            buttonBye.isVisible = loading.not()
            progressBar.isVisible = loading
        }
    }

    data class LoadProduct(
            var product: Product,
            var loading: Boolean = false
    )
}
