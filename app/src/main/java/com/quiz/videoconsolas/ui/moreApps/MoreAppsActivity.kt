package com.quiz.videoconsolas.ui.moreApps

import android.os.Bundle
import android.view.View
import com.quiz.videoconsolas.R
import com.quiz.videoconsolas.base.BaseActivity
import com.quiz.videoconsolas.databinding.MoreAppsActivityBinding
import com.quiz.videoconsolas.utils.setSafeOnClickListener
import com.quiz.videoconsolas.common.viewBinding

class MoreAppsActivity : BaseActivity() {
    private val binding by viewBinding(MoreAppsActivityBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.more_apps_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.containerResult, MoreAppsFragment.newInstance())
                .commitNow()
        }
        setupToolbar()

        binding.appBar.btnBack.setSafeOnClickListener {
            finish()
        }
        binding.appBar.layoutExtendedTitle.background = null
        binding.appBar.toolbarTitle.text = getString(R.string.more_apps)
        binding.appBar.layoutLife.visibility = View.GONE
    }

    private fun setupToolbar() {
        binding.appBar.toolbarTitle.text = getString(R.string.ranking_screen_title)
        binding.appBar.layoutLife.visibility = View.GONE
        binding.appBar.layoutExtendedTitle.background = null
        binding.appBar.btnBack.setSafeOnClickListener { finishAfterTransition() }
    }
}