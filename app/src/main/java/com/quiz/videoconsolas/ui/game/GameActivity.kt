package com.quiz.videoconsolas.ui.game

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.quiz.videoconsolas.R
import com.quiz.videoconsolas.base.BaseActivity
import com.quiz.videoconsolas.utils.setSafeOnClickListener
import com.quiz.videoconsolas.utils.showBanner
import com.quiz.videoconsolas.utils.showBonificado
import com.quiz.videoconsolas.common.viewBinding
import com.quiz.videoconsolas.databinding.GameActivityBinding


class GameActivity : BaseActivity() {
    private val binding by viewBinding(GameActivityBinding::inflate)
    private var rewardedAd: RewardedAd? = null
    private lateinit var activity: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.containerGame, GameFragment.newInstance())
                    .commitNow()
        }
        activity = this

        MobileAds.initialize(this)
        RewardedAd.load(this, getString(R.string.BONIFICADO_GAME), AdRequest.Builder().build(), object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d("GameActivity", adError.toString())
                FirebaseCrashlytics.getInstance().recordException(Throwable(adError.message))
                rewardedAd = null
            }

            override fun onAdLoaded(ad: RewardedAd) {
                Log.d("GameActivity", "Ad was loaded.")
                rewardedAd = ad
            }
        })

        binding.appBar.btnBack.setSafeOnClickListener {
            finishAfterTransition()
        }

        binding.appBar.layoutExtendedTitle.background = null
        binding.appBar.layoutLife.visibility = View.VISIBLE
        writePoints(0)
    }

    fun writePoints(points: Int) {
        runOnUiThread {
            binding.appBar.toolbarTitle.text = getString(R.string.points, points)
            binding.appBar.toolbarTitle.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_xy_collapse))
        }
    }

    fun writeLife(life: Int) {
        runOnUiThread {
            when (life) {
                2 -> {
                    binding.appBar.lifeSecond.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_life_on))
                    binding.appBar.lifeFirst.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_life_on))
                }
                1 -> {
                    binding.appBar.lifeSecond.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.scale_xy_collapse))
                    binding.appBar.lifeSecond.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_life_off))
                    binding.appBar.lifeFirst.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_life_on))
                }
                0 -> {
                    binding.appBar.lifeFirst.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.scale_xy_collapse))

                    // GAME OVER
                    binding.appBar.lifeSecond.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_life_off))
                    binding.appBar.lifeFirst.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_life_off))
                }
            }
        }
    }

    fun showBannerAd(show: Boolean){
        showBanner(show, binding.adViewGame)
    }

    fun showRewardedAd(show: Boolean){
        showBonificado(this, show, rewardedAd)
    }
}