package io.john6.appbase.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import io.john6.appbase.databinding.FragmentMainBinding
import io.john6.appbase.ui.adapter.MainListAdapter
import io.john6.appbase.vm.MainViewModel
import io.john6.base.util.JInsetsHelper
import io.john6.base.util.safeDrawing

class MainFragment : Fragment() {
    private val mViewModel by viewModels<MainViewModel>()

    private var _mBinding: FragmentMainBinding? = null
    private val mBinding get() = _mBinding!!
    private val JInsetsHelper = JInsetsHelper().apply {
        enforceNavBar = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(JInsetsHelper)
        mViewModel.initNavGraphData(findNavController())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentMainBinding.inflate(inflater, container, false).let {
        _mBinding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.recyclerViewFgMain.adapter = MainListAdapter()
        mViewModel.getAvailablePage().observe(viewLifecycleOwner, this::onListDataChanged)
        JInsetsHelper.onInsetsChanged = { insets ->
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