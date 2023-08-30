package com.quiz.videoconsolas.ui.moreApps

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.quiz.domain.App
import com.quiz.videoconsolas.R
import com.quiz.videoconsolas.databinding.MoreAppsFragmentBinding
import com.quiz.videoconsolas.utils.glideLoadingGif
import com.quiz.videoconsolas.utils.showBanner
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoreAppsFragment : Fragment() {
    private lateinit var binding: MoreAppsFragmentBinding
    private val moreAppsViewModel: MoreAppsViewModel by viewModel()
    private var rewardedAd: RewardedAd? = null

    companion object {
        fun newInstance() = MoreAppsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = MoreAppsFragmentBinding.inflate(inflater)
        val root = binding.root

        MobileAds.initialize(requireContext())
        RewardedAd.load(requireContext(), getString(R.string.BONIFICADO_GAME), AdRequest.Builder().build(), object : RewardedAdLoadCallback() {
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

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        moreAppsViewModel.list.observe(viewLifecycleOwner, Observer(::fillList))
        moreAppsViewModel.navigation.observe(viewLifecycleOwner, Observer(::navigate))
        moreAppsViewModel.progress.observe(viewLifecycleOwner, Observer(::updateProgress))
        moreAppsViewModel.showingAds.observe(viewLifecycleOwner, Observer(::loadAd))
    }

    private fun fillList(appList: MutableList<App>) {
        binding.recyclerviewMoreApps.adapter = MoreAppsListAdapter(requireContext(), appList)
    }

    private fun updateProgress(model: MoreAppsViewModel.UiModel?) {
        if (model is MoreAppsViewModel.UiModel.Loading && model.show) {
            glideLoadingGif(activity as MoreAppsActivity, binding.imagenLoading)
            binding.imagenLoading.visibility = View.VISIBLE
        } else {
            binding.imagenLoading.visibility = View.GONE
        }
    }

    private fun navigate(navigation: MoreAppsViewModel.Navigation) {
        when (navigation) {
            MoreAppsViewModel.Navigation.Result -> { activity?.finishAfterTransition() }
        }
    }

    private fun loadAd(model: MoreAppsViewModel.UiModel) {
        if (model is MoreAppsViewModel.UiModel.ShowAd) {
            showBanner(model.show, binding.adViewMoreApps)
        }
    }
}
