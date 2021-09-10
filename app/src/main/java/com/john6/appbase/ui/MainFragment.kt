package com.john6.appbase.ui

import android.os.Bundle
import android.view.View
import com.john6.appbase.base.BaseFragment
import com.john6.appbase.databinding.FragmentMainBinding
import com.john6.appbase.log

class MainFragment:BaseFragment<FragmentMainBinding>() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        "onViewCreated".log()
    }
}