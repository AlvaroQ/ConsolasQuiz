package com.quiz.videoconsolas.ui.select

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
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
import org.koin.android.scope.lifecycleScope
import org.koin.android.viewmodel.scope.viewModel


class SelectFragment : Fragment() {
    private var loadAd: Boolean = true
    private lateinit var rewardedAd: RewardedAd
    private lateinit var binding: SelectFragmentBinding
    private val selectViewModel: SelectViewModel by lifecycleScope.viewModel(this)

    companion object {
        fun newInstance() = SelectFragment()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = SelectFragmentBinding.inflate(inflater)
        val root = binding.root

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

    private fun navigate(navigation: SelectViewModel.Navigation?) {
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
        rewardedAd = RewardedAd(requireContext(), getString(R.string.BONIFICADO_SHOW_INFO))
        val adLoadCallback: RewardedAdLoadCallback = object: RewardedAdLoadCallback() {
            override fun onRewardedAdLoaded() {
                rewardedAd.show(activity, null)
            }
            override fun onRewardedAdFailedToLoad(adError: LoadAdError) {
                FirebaseCrashlytics.getInstance().recordException(Throwable(adError.message))
            }
        }
        rewardedAd.loadAd(AdRequest.Builder().build(), adLoadCallback)
    }
}
