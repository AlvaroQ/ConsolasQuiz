package com.quiz.videoconsolas.ui.ranking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.quiz.domain.User
import com.quiz.videoconsolas.R
import com.quiz.videoconsolas.databinding.RankingFragmentBinding
import com.quiz.videoconsolas.utils.glideLoadingGif
import com.quiz.videoconsolas.utils.showBanner
import org.koin.androidx.viewmodel.ext.android.viewModel


class RankingFragment : Fragment() {
    private lateinit var binding: RankingFragmentBinding
    private val rankingViewModel: RankingViewModel by viewModel()
    private var rewardedAd: RewardedAd? = null

    companion object {
        fun newInstance() = RankingFragment()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = RankingFragmentBinding.inflate(inflater)
        val root = binding.root

        MobileAds.initialize(requireContext())

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rankingViewModel.rankingList.observe(viewLifecycleOwner, Observer(::fillRanking))
        rankingViewModel.navigation.observe(viewLifecycleOwner, Observer(::navigate))
        rankingViewModel.progress.observe(viewLifecycleOwner, Observer(::updateProgress))
        rankingViewModel.showingAds.observe(viewLifecycleOwner, Observer(::loadAd))
    }

    private fun fillRanking(userList: MutableList<User>) {
        binding.recyclerviewRanking.adapter = RankingListAdapter(requireContext(), userList)
    }

    private fun updateProgress(model: RankingViewModel.UiModel?) {
        if (model is RankingViewModel.UiModel.Loading && model.show) {
            glideLoadingGif(activity as RankingActivity, binding.imagenLoading)
            binding.imagenLoading.visibility = View.VISIBLE
        } else {
            binding.imagenLoading.visibility = View.GONE
        }
    }

    private fun navigate(navigation: RankingViewModel.Navigation) {
        when (navigation) {
            RankingViewModel.Navigation.Result -> {
                activity?.finish()
            }
        }
    }

    private fun loadAd(model: RankingViewModel.UiModel) {
        if (model is RankingViewModel.UiModel.ShowAd) {
            showBanner(model.show, binding.adViewRanking)
        }
    }
}
