package com.quiz.videoconsolas.ui.info

import android.os.Bundle
import android.view.View
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.quiz.videoconsolas.R
import com.quiz.videoconsolas.base.BaseActivity
import com.quiz.videoconsolas.utils.setSafeOnClickListener
import kotlinx.android.synthetic.main.app_bar_layout.*
import kotlinx.android.synthetic.main.info_activity.*

class InfoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.info_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.containerInfo, InfoFragment.newInstance())
                .commitNow()
        }

        btnBack.setSafeOnClickListener { finishAfterTransition() }
        toolbarTitle.text = getString(R.string.info_title)
        layoutLife.visibility = View.GONE
    }

    fun showAd(show: Boolean){
        if(show) {
            MobileAds.initialize(this)
            val adRequest = AdRequest.Builder().build()
            adViewInfo.loadAd(adRequest)
        } else {
            adViewInfo.visibility = View.GONE
        }
    }
}