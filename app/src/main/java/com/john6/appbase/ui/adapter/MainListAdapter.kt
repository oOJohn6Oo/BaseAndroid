package com.john6.appbase.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.john6.johnbase.util.getAttrResId
import com.john6.johnbase.util.vdp

class MainListAdapter() :
    Adapter<RecyclerView.ViewHolder>() {

    private var dataList: List<NavDestination> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newDataList: List<NavDestination>) {
        dataList = newDataList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return object : RecyclerView.ViewHolder(TextView(parent.context).also {
            it.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            val bgdId = it.context.getAttrResId(android.R.attr.selectableItemBackground)
            it.setBackgroundResource(bgdId)
            it.textSize = 16f
            it.setPadding(20.vdp.toInt())
        }) {}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemView = holder.itemView as TextView
        val itemData = dataList[position]
        itemView.text = itemData.label
        itemView.setOnClickListener {
            it.findNavController().navigate(itemData.id)
        }
    }

    override fun getItemCount() = dataList.size
}