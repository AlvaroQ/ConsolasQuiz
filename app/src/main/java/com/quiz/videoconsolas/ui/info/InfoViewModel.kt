package com.quiz.videoconsolas.ui.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.quiz.domain.Console
import com.quiz.videoconsolas.common.ScopedViewModel
import com.quiz.videoconsolas.managers.AnalyticsManager
import com.quiz.usecases.GetPaymentDone
import com.quiz.usecases.GetConsolaList
import kotlinx.coroutines.launch

class InfoViewModel(private val getConsolaList: GetConsolaList,
                    private val getPaymentDone: GetPaymentDone) : ScopedViewModel() {
    private var list = mutableListOf<Console>()

    private val _progress = MutableLiveData<UiModel>()
    val progress: LiveData<UiModel> = _progress

    private val _navigation = MutableLiveData<Navigation>()
    val navigation: LiveData<Navigation> = _navigation

    private val _prideList = MutableLiveData<MutableList<Console>>()
    val consoleList: LiveData<MutableList<Console>> = _prideList

    private val _updatePrideList = MutableLiveData<MutableList<Console>>()
    val updateConsoleList: LiveData<MutableList<Console>> = _updatePrideList

    private val _showingAds = MutableLiveData<UiModel>()
    val showingAds: LiveData<UiModel> = _showingAds

    init {
        AnalyticsManager.analyticsScreenViewed(AnalyticsManager.SCREEN_INFO)
        launch {
            _progress.value = UiModel.Loading(true)
            _prideList.value = getPrideList(0)
            _showingAds.value = UiModel.ShowAd(!getPaymentDone())
            _progress.value = UiModel.Loading(false)
        }
    }

    fun loadMorePrideList(currentPage: Int) {
        launch {
            _progress.value = UiModel.Loading(true)
            _updatePrideList.value = getPrideList(currentPage)
            _progress.value = UiModel.Loading(false)
        }
    }

    private suspend fun getPrideList(currentPage: Int): MutableList<Console> {
        list = (list + getConsolaList.invoke(currentPage)) as MutableList<Console>
        return list
    }

    fun navigateToSelect() {
        _navigation.value = Navigation.Select
    }

    sealed class Navigation {
        object Select : Navigation()
    }

    sealed class UiModel {
        data class Loading(val show: Boolean) : UiModel()
        data class ShowAd(val show: Boolean) : UiModel()
    }
}