package com.quiz.videoconsolas.ui.result

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.quiz.videoconsolas.R
import com.quiz.videoconsolas.base.BaseActivity
import com.quiz.videoconsolas.common.startActivity
import com.quiz.videoconsolas.ui.select.SelectActivity
import com.quiz.videoconsolas.utils.setSafeOnClickListener
import com.quiz.videoconsolas.common.viewBinding
import com.quiz.videoconsolas.databinding.ResultActivityBinding
import com.quiz.videoconsolas.utils.showBonificado

class ResultActivity : BaseActivity() {
    private val binding by viewBinding(ResultActivityBinding::inflate)
    private lateinit var activity: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.containerResult, ResultFragment.newInstance())
                .commitNow()
        }
        activity = this
        setupToolbar()

        binding.appBar.btnBack.setSafeOnClickListener {
            startActivity<SelectActivity> {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            }
        }
        binding.appBar.toolbarTitle.text = getString(R.string.resultado_screen_title)
        binding.appBar.layoutLife.visibility = View.GONE
    }

    private fun setupToolbar() {
        binding.appBar.toolbarTitle.text = getString(R.string.result)
        binding.appBar.layoutLife.visibility = View.GONE
        binding.appBar.layoutExtendedTitle.background = null
        binding.appBar.btnBack.setSafeOnClickListener { finishAfterTransition() }
    }
}