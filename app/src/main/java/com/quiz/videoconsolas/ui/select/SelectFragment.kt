package com.quiz.videoconsolas.ui.select

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.quiz.videoconsolas.R
import com.quiz.videoconsolas.common.startActivity
import com.quiz.videoconsolas.databinding.SelectFragmentBinding
import com.quiz.videoconsolas.ui.game.GameActivity
import com.quiz.videoconsolas.ui.info.InfoActivity
import com.quiz.videoconsolas.ui.settings.SettingsActivity
import com.quiz.videoconsolas.utils.setSafeOnClickListener
import com.quiz.videoconsolas.utils.showBonificado
import org.koin.androidx.viewmodel.ext.android.viewModel


class SelectFragment : Fragment() {
    private var loadAd: Boolean = true
    private var rewardedAd: RewardedAd? = null
    private lateinit var binding: SelectFragmentBinding
    private val selectViewModel: SelectViewModel by viewModel()

    companion object {
        fun newInstance() = SelectFragment()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = SelectFragmentBinding.inflate(inflater)
        val root = binding.root


        MobileAds.initialize(requireContext())
        RewardedAd.load(requireContext(), getString(R.string.BONIFICADO_SHOW_INFO), AdRequest.Builder().build(), object : RewardedAdLoadCallback() {
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

        val cardStart: CardView = root.findViewById(R.id.cardStart)
        cardStart.setSafeOnClickListener {
            selectViewModel.navigateToGame()
        }

        val cardLearn: CardView = root.findViewById(R.id.cardLearn)
        cardLearn.setSafeOnClickListener {
            selectViewModel.navigateToInfo()
        }

        val cardSettings: CardView = root.findViewById(R.id.cardSettings)
        cardSettings.setSafeOnClickListener {
            selectViewModel.navigateToSettings()
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectViewModel.navigation.observe(viewLifecycleOwner, Observer(::navigate))
        selectViewModel.showingAds.observe(viewLifecycleOwner, Observer(::loadAd))
    }

    override fun onResume() {
        super.onResume()
        selectViewModel.updateShowingAd()
    }

    private fun navigate(navigation: SelectViewModel.Navigation) {
        when (navigation) {
            SelectViewModel.Navigation.Game -> activity?.startActivity<GameActivity> {}
            SelectViewModel.Navigation.Settings -> activity?.startActivity<SettingsActivity> {}
            SelectViewModel.Navigation.Info -> {
                if(loadAd) loadRewardedAd()
                activity?.startActivity<InfoActivity> {}
            }
        }
    }

    private fun loadAd(model: SelectViewModel.UiModel) {
        if (model is SelectViewModel.UiModel.ShowAd)
            loadAd = model.show
    }


    private fun loadRewardedAd() {
        showBonificado(requireActivity(), true, rewardedAd)
    }
}
