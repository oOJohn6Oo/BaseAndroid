package com.john6.appbase.ui.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import androidx.annotation.Size
import androidx.core.view.setPadding
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.john6.appbase.app
import io.john6.johnbase.util.getAttrResId
import io.john6.johnbase.util.vdp

class MainListAdapter : Adapter<RecyclerView.ViewHolder>() {

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
            it.findNavController().navigate(itemData.id, null, navOptionWithSystemActivityAnimation)
        }
    }

    override fun getItemCount() = dataList.size
}

/**
 * 获取系统默认的 Activity 动画
 */
@Suppress("ResourceType", "unused")
@get:Size(4)
val systemDefaultActivityAnimation by lazy {
    val windowAnimation = app.theme.obtainStyledAttributes(intArrayOf(android.R.attr.windowAnimationStyle))
    val windowRes: Int = windowAnimation.getResourceId(0, 0)
    val activityAnimation = app.theme.obtainStyledAttributes(
        windowRes,
        intArrayOf(
            android.R.attr.activityOpenEnterAnimation,
            android.R.attr.activityOpenExitAnimation,
            android.R.attr.activityCloseEnterAnimation,
            android.R.attr.activityCloseExitAnimation
        )
    )
    intArrayOf(
        activityAnimation.getResourceId(0, 0),
        activityAnimation.getResourceId(1, 0),
        activityAnimation.getResourceId(2, 0),
        activityAnimation.getResourceId(3, 0),
    )
}

/**
 * 带有系统 Activity 动画的 [NavOptions]
 */
val navOptionWithSystemActivityAnimation by lazy {
    navOptions {
        anim {
            enter = systemDefaultActivityAnimation[0]
            exit = systemDefaultActivityAnimation[1]
            popEnter = systemDefaultActivityAnimation[2]
            popExit = systemDefaultActivityAnimation[3]
        }
    }
}