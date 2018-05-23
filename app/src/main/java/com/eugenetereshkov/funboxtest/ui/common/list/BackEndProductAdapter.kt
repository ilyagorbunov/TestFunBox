package com.eugenetereshkov.funboxtest.ui.common.list

import android.os.Bundle
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.eugenetereshkov.funboxtest.R
import com.eugenetereshkov.funboxtest.data.entity.Product
import com.eugenetereshkov.funboxtest.extension.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_back_end_product.*

class BackEndProductAdapter(
        private val listener: (index: Int) -> Unit
) : ListAdapter<Product, BackEndProductAdapter.ViewHolder>(ProductDiffUtilsCallBack) {

    private object ProductDiffUtilsCallBack : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean = true

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean = oldItem == newItem

        override fun getChangePayload(oldItem: Product, newItem: Product): Any = Bundle().apply {
            if (oldItem.name != newItem.name) putString(Product.NAME, newItem.name)
            if (oldItem.count != newItem.count) putInt(Product.COUNT, newItem.count)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_back_end_product), listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
            return
        }

        (payloads[0] as Bundle).keySet().forEach { key ->
            val item = getItem(position)
            if (key == Product.NAME) holder.bindName(item.name)
            if (key == Product.COUNT) holder.bindCount(item.count)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, getItem(position))
    }

    class ViewHolder(
            override val containerView: View,
            listener: (index: Int) -> Unit
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private var index: Int = RecyclerView.NO_POSITION
        private lateinit var product: Product

        init {
            itemView.setOnClickListener { listener(index) }
        }

        fun bind(position: Int, item: Product) {
            this.index = position
            this.product = item
            bindName(item.name)
            textViewCount.text = itemView.context.getString(R.string.count_format, item.count)
        }

        fun bindName(name: String) {
            product.name = name
            textViewName.text = name
        }

        fun bindCount(count: Int) {
            product.count = count
            textViewCount.run { text = context.getString(R.string.count_format, count) }
        }
    }
}