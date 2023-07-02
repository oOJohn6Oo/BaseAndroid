package com.john6.appbase.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import com.john6.appbase.databinding.FragmentMainBinding
import com.john6.appbase.ui.adapter.MainListAdapter
import com.john6.appbase.vm.MainViewModel
import com.john6.johnbase.util.InsetsHelper
import com.john6.johnbase.util.safeContent
import com.john6.johnbase.util.safeDrawing

class MainFragment : Fragment() {
    private val mViewModel by viewModels<MainViewModel>()

    private var _mBinding: FragmentMainBinding? = null
    private val mBinding get() = _mBinding!!
    private val insetsHelper = InsetsHelper().apply {
        enforceNavBar = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(insetsHelper)
        mViewModel.initNavGraphData(findNavController())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentMainBinding.inflate(inflater, container, false).also {
        _mBinding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.recyclerViewFgMain.adapter = MainListAdapter()
        mViewModel.getAvailablePage().observe(viewLifecycleOwner, this::onListDataChanged)
        insetsHelper.onInsetsChanged = { insets ->
            insets.safeDrawing().apply {
                mBinding.recyclerViewFgMain.setPaddingRelative(left, 0, right, bottom)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }

    private fun onListDataChanged(newDataList: List<NavDestination>) {
        (mBinding.recyclerViewFgMain.adapter as? MainListAdapter)?.submitList(newDataList)
    }

}