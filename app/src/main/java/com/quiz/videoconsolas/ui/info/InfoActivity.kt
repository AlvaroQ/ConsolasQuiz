package com.quiz.videoconsolas.ui.info

import android.os.Bundle
import android.view.View
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.quiz.videoconsolas.R
import com.quiz.videoconsolas.base.BaseActivity
import com.quiz.videoconsolas.databinding.InfoActivityBinding
import com.quiz.videoconsolas.utils.setSafeOnClickListener
import com.quiz.videoconsolas.common.viewBinding
import com.quiz.videoconsolas.utils.showBanner

class InfoActivity : BaseActivity() {
    private val binding by viewBinding(InfoActivityBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.containerInfo, InfoFragment.newInstance())
                .commitNow()
        }

        MobileAds.initialize(this)

        binding.appBar.btnBack.setSafeOnClickListener { finishAfterTransition() }
        binding.appBar.toolbarTitle.text = getString(R.string.info_title)
        binding.appBar.layoutLife.visibility = View.GONE
    }

    fun showAd(show: Boolean){
        showBanner(show, binding.adViewInfo)
    }
}