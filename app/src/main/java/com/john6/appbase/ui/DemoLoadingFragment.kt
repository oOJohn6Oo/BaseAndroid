package com.john6.appbase.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.john6.appbase.databinding.FragmentDemoLoadingBinding
import com.john6.johnbase.util.ProgressHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class DemoLoadingFragment: Fragment() {

    private var _mBinding:FragmentDemoLoadingBinding? = null
    private val mBinding get() = _mBinding!!

    private var job:Job? = null
    private var toast:Toast? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentDemoLoadingBinding.inflate(inflater, container, false).let {
        _mBinding = it
        it.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.btnFgDemoLoading.setOnClickListener(this::showLoading)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun showLoading(view: View?) {
        job?.cancel()
        toast?.cancel()
        toast = Toast.makeText(requireContext(), "task start", Toast.LENGTH_SHORT)
        toast?.show()

        ProgressHelper.show(requireContext())
        val delay = mBinding.seekBarFgDemoLoading.progress.toLong()
        job = lifecycleScope.launch {
            delay(delay)
            if (this.isActive) {
                toast?.cancel()
                toast = Toast.makeText(requireContext(), "task time: ${delay}ms", Toast.LENGTH_SHORT)
                toast?.show()
                ProgressHelper.dismiss(requireContext())
            }
        }
    }
}