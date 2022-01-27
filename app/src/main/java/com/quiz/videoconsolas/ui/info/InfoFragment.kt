package com.quiz.videoconsolas.ui.info

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.quiz.domain.Console
import com.quiz.videoconsolas.common.startActivity
import com.quiz.videoconsolas.databinding.InfoFragmentBinding
import com.quiz.videoconsolas.ui.select.SelectActivity
import com.quiz.videoconsolas.utils.Constants.TOTAL_ITEM_EACH_LOAD
import com.quiz.videoconsolas.utils.Constants.TOTAL_PRIDES
import com.quiz.videoconsolas.utils.glideLoadingGif
import org.koin.android.scope.lifecycleScope
import org.koin.android.viewmodel.scope.viewModel


class InfoFragment : Fragment() {
    private lateinit var binding: InfoFragmentBinding
    private val infoViewModel: InfoViewModel by lifecycleScope.viewModel(this)
    private var currentPage = 0
    private lateinit var scrollListener: RecyclerView.OnScrollListener
    lateinit var adapter: InfoListAdapter

    companion object {
        fun newInstance() = InfoFragment()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = InfoFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        infoViewModel.navigation.observe(viewLifecycleOwner, Observer(::navigate))
        infoViewModel.consoleList.observe(viewLifecycleOwner, Observer(::fillPrideList))
        infoViewModel.updateConsoleList.observe(viewLifecycleOwner, Observer(::updatePrideList))
        infoViewModel.progress.observe(viewLifecycleOwner, Observer(::updateProgress))
        infoViewModel.showingAds.observe(viewLifecycleOwner, Observer(::loadAd))
    }

    private fun loadAd(model: InfoViewModel.UiModel) {
        if (model is InfoViewModel.UiModel.ShowAd)
            (activity as InfoActivity).showAd(model.show)
    }

    private fun updateProgress(model: InfoViewModel.UiModel?) {
        if (model is InfoViewModel.UiModel.Loading && model.show) {
            glideLoadingGif(activity as InfoActivity, binding.imagenLoading)
            binding.imagenLoading.visibility = View.VISIBLE
        } else {
            binding.imagenLoading.visibility = View.GONE
        }
    }

    private fun fillPrideList(consoleList: MutableList<Console>) {
        adapter = InfoListAdapter(requireContext(), consoleList)
        binding.recyclerviewInfo.adapter = adapter
        setRecyclerViewScrollListener()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updatePrideList(consoleList: MutableList<Console>) {
        adapter.update(consoleList)
        adapter.notifyDataSetChanged()
        setRecyclerViewScrollListener()
    }

    private fun setRecyclerViewScrollListener() {
        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalItemCount = recyclerView.layoutManager?.itemCount
                val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

                if (totalItemCount == lastVisibleItemPosition + 1) {
                    binding.recyclerviewInfo.removeOnScrollListener(scrollListener)

                    if(currentPage * TOTAL_ITEM_EACH_LOAD < TOTAL_PRIDES) {
                        Log.d("MyTAG", "Load new list")
                        currentPage++
                        infoViewModel.loadMorePrideList(currentPage)
                    }
                }
            }
        }
        binding.recyclerviewInfo.addOnScrollListener(scrollListener)
    }

    private fun navigate(navigation: InfoViewModel.Navigation?) {
        when (navigation) {
            InfoViewModel.Navigation.Select -> {
                activity?.startActivity<SelectActivity> {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                }
            }
            else -> {}
        }
    }
}
