package com.john6.appbase.base

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseSimpleListAdapter<T,V:View>(var dataList:List<T>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return object :RecyclerView.ViewHolder(onCreateView(parent,viewType)){}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindView(holder, holder.itemView as V, position, dataList[position])
    }

    override fun getItemCount() = dataList.size

    abstract fun onCreateView(parent: ViewGroup, viewType: Int):V
    abstract fun onBindView(holder: RecyclerView.ViewHolder,itemView:V, position: Int, itemData:T)

}