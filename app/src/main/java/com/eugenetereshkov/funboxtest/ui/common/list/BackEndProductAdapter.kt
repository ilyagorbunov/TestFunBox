package com.eugenetereshkov.funboxtest.ui.common.list

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
        private val listener: (Product) -> Unit
) : ListAdapter<Product, BackEndProductAdapter.ViewHolder>(ProductDiffUtilsCallBack) {

    private object ProductDiffUtilsCallBack : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
                oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
                oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_back_end_product), listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
            override val containerView: View,
            listener: (Product) -> Unit
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private lateinit var item: Product

        init {
            itemView.setOnClickListener { listener(item) }
        }

        fun bind(item: Product) {
            this.item = item

            textViewName.text = item.name
            textViewCount.text = item.count.toString()
        }
    }
}