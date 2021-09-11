package com.john6.appbase.base

import androidx.annotation.Keep
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

@Keep
abstract class BaseViewHolder<T,VB : ViewBinding>(private val layoutBinding:VB):RecyclerView.ViewHolder(layoutBinding.root) {
    abstract fun creating()
    abstract fun binding(itemData:T)
}