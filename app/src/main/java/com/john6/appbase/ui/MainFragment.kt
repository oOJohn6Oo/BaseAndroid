package com.john6.appbase.ui

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.fragment.app.viewModels
import androidx.navigation.NavDestination
import androidx.recyclerview.widget.RecyclerView
import com.john6.appbase.R
import com.john6.appbase.base.BaseFragment
import com.john6.appbase.databinding.FragmentMainBinding
import com.john6.appbase.dp
import com.john6.appbase.getAttrResId
import com.john6.appbase.vm.MainViewModel

class MainFragment:BaseFragment<FragmentMainBinding>() {
    private val mModel by viewModels<MainViewModel>()

    private var dataList = listOf<NavDestination>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mModel.initNavGraphData(navController)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mModel.getAvailablePage().observe(viewLifecycleOwner){
            dataList = it
            mBinding.recyclerViewFgMain.adapter?.notifyDataSetChanged()
        }
        mBinding.recyclerViewFgMain.adapter = object :RecyclerView.Adapter<RecyclerView.ViewHolder>(){
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): RecyclerView.ViewHolder {
                return object :RecyclerView.ViewHolder(TextView(parent.context).also {
                    it.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                    val bgdId = it.context.getAttrResId(R.attr.selectableItemBackground)
                    it.setBackgroundResource(bgdId)
                    it.setPadding(12.dp.toInt())
                }){}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                (holder.itemView as TextView).text = dataList[position].label
                holder.itemView.setOnClickListener {
                    navController.navigate(dataList[position].id)
                }
            }

            override fun getItemCount() = dataList.size

        }
    }
}