package com.john6.appbase.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.john6.appbase.getGenericType

@Keep
class BaseListAdapter<T, VB:ViewBinding, H:BaseViewHolder<T,VB>>(val dataList:List<T>,private val holderClass:Class<H>):RecyclerView.Adapter<H>() {
    private var classVB = holderClass.getGenericType(1)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): H {
        val binding = classVB.getDeclaredMethod("inflate",LayoutInflater::class.java,ViewGroup::class.java,Boolean::class.java) as VB

        val holder = holderClass.getConstructor(classVB).newInstance(binding)
        holder.creating()

        return holder
    }

    override fun onBindViewHolder(holder: H, position: Int) {
        holder.binding(dataList[position])
    }

    override fun getItemCount() = dataList.size

}