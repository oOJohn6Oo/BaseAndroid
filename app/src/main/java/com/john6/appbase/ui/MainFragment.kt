package com.john6.appbase.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavDestination
import com.john6.appbase.base.BaseFragment
import com.john6.appbase.databinding.FragmentMainBinding
import com.john6.appbase.observe
import com.john6.appbase.ui.adapter.MainListAdapter
import com.john6.appbase.vm.MainViewModel

class MainFragment : BaseFragment<FragmentMainBinding>() {
    private val mModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mModel.initNavGraphData(navController)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initObserve()
    }

    private fun initView() {
        mBinding.recyclerViewFgMain.adapter = MainListAdapter(emptyList())
    }

    private fun initObserve() {
        observe(mModel.getAvailablePage(), this::onListDataChanged)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onListDataChanged(newDataList: List<NavDestination>) {
        mBinding.recyclerViewFgMain.adapter?.also {
            (it as MainListAdapter).dataList = newDataList
            it.notifyDataSetChanged()
        }
    }

}