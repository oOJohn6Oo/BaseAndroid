package com.john6.appbase.ui.adapter

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.john6.appbase.R
import com.john6.appbase.base.BaseSimpleListAdapter
import com.john6.appbase.dp
import com.john6.appbase.getAttrResId

class MainListAdapter(dataList: List<NavDestination>) :
    BaseSimpleListAdapter<NavDestination, TextView>(dataList) {
    override fun onCreateView(parent: ViewGroup, viewType: Int) = TextView(parent.context).also {
        it.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        val bgdId = it.context.getAttrResId(R.attr.selectableItemBackground)
        it.setBackgroundResource(bgdId)
        it.textSize = 16f
        it.setPadding(20.dp.toInt())
    }

    override fun onBindView(
        holder: RecyclerView.ViewHolder,
        itemView: TextView,
        position: Int,
        itemData: NavDestination
    ) {
        itemView.text = itemData.label
        itemView.setOnClickListener {
            it.findNavController().navigate(itemData.id)
        }
    }

}