package com.john6.appbase

import android.os.Bundle
import com.john6.appbase.base.BaseActivity
import com.john6.appbase.databinding.ActivityMainBinding

/**
 * Since this APP is One-Activity Multi-Fragment
 * Actually we don't need a Base
 * But just in case.
 */
class MainActivity : BaseActivity() {
    private lateinit var mBinding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }
}