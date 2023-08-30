package com.quiz.videoconsolas.ui.settings

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.google.android.gms.ads.MobileAds
import com.quiz.videoconsolas.R
import com.quiz.videoconsolas.base.BaseActivity
import com.quiz.videoconsolas.databinding.SettingsActivityBinding
import com.quiz.videoconsolas.utils.setSafeOnClickListener
import com.quiz.videoconsolas.common.viewBinding
import com.quiz.videoconsolas.utils.showBanner


class SettingsActivity : BaseActivity() {
    private val binding by viewBinding(SettingsActivityBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupToolbar()
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        MobileAds.initialize(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                finishAfterTransition()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupToolbar() {
        binding.appBar.toolbarTitle.text = getString(R.string.settings)
        binding.appBar.layoutLife.visibility = View.GONE
        binding.appBar.layoutExtendedTitle.background = null
        binding.appBar.btnBack.setSafeOnClickListener { finishAfterTransition() }
    }
}