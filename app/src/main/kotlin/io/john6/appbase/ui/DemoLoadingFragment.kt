package io.john6.appbase.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import io.john6.appbase.databinding.FragmentDemoLoadingBinding
import io.john6.base.util.JInsetsHelper
import io.john6.base.util.JLoadingHelper
import io.john6.base.util.safeDrawing
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class DemoLoadingFragment: Fragment() {

    private var _mBinding:FragmentDemoLoadingBinding? = null
    private val mBinding get() = _mBinding!!

    private var job:Job? = null
    private var toast:Toast? = null

    private val mJInsetsHelper = JInsetsHelper().apply {
        onInsetsChanged = this@DemoLoadingFragment::onInsetsChanged
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(mJInsetsHelper)
    }

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
        mBinding.seekBarFgDemoLoading.setLabelFormatter {
            "${it.toLong()}ms"
        }
    }

    private fun onInsetsChanged(insets: WindowInsetsCompat) {
        insets.safeDrawing().apply {
            mBinding.root.setPaddingRelative(left, top, right, bottom)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun showLoading(view: View?) {
        job?.cancel()
        toast?.cancel()
        toast = Toast.makeText(requireContext(), "task start", Toast.LENGTH_SHORT)
        toast?.show()

        JLoadingHelper.show(requireContext())
        val delay = mBinding.seekBarFgDemoLoading.value.toLong()
        job = lifecycleScope.launch {
            delay(delay)
            if (this.isActive) {
                toast?.cancel()
                toast = Toast.makeText(requireContext(), "task time: ${delay}ms", Toast.LENGTH_SHORT)
                toast?.show()
                JLoadingHelper.dismiss(requireContext())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        JLoadingHelper.dismiss(requireContext())
    }
}