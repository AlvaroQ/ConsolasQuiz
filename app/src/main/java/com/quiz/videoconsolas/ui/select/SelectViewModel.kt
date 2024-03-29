package com.quiz.videoconsolas.ui.select

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.quiz.videoconsolas.common.ScopedViewModel
import com.quiz.videoconsolas.managers.AnalyticsManager
import com.quiz.usecases.GetPaymentDone

class SelectViewModel(private val getPaymentDone: GetPaymentDone) : ScopedViewModel() {

    private val _navigation = MutableLiveData<Navigation>()
    val navigation: LiveData<Navigation> = _navigation

    private val _showingAds = MutableLiveData<UiModel>()
    val showingAds: LiveData<UiModel> = _showingAds

    init {
        AnalyticsManager.analyticsScreenViewed(AnalyticsManager.SCREEN_SELECT_GAME)
        updateShowingAd()
    }

     fun updateShowingAd()  {
        _showingAds.value = UiModel.ShowAd(!getPaymentDone())
    }

    private fun getPaymentDone(): Boolean {
        return getPaymentDone.invoke()
    }

    fun navigateToGame() {
        _navigation.value = Navigation.Game
    }

    fun navigateToSettings() {
        _navigation.value = Navigation.Settings
    }

    fun navigateToInfo() {
        _navigation.value = Navigation.Info
    }

    sealed class Navigation {
        object Game : Navigation()
        object Info : Navigation()
        object Settings : Navigation()
    }

    sealed class UiModel {
        data class ShowAd(val show: Boolean) : UiModel()
    }
}