package com.quiz.videoconsolas.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.quiz.domain.Console
import com.quiz.videoconsolas.R
import com.quiz.videoconsolas.common.ResourceProvider
import com.quiz.videoconsolas.common.ScopedViewModel
import com.quiz.videoconsolas.managers.AnalyticsManager
import com.quiz.videoconsolas.utils.Constants.TOTAL_PRIDES
import com.quiz.usecases.GetPaymentDone
import com.quiz.usecases.GetConsolaById
import kotlinx.coroutines.launch

class GameViewModel(private val getConsolaById: GetConsolaById,
                    private val resourceProvider: ResourceProvider,
                    private val getPaymentDone: GetPaymentDone) : ScopedViewModel() {
    private var randomCountries = mutableListOf<Int>()
    private lateinit var console: Console

    private val _question = MutableLiveData<Console>()
    val question: LiveData<Console> = _question

    private val _responseOptions = MutableLiveData<MutableList<String>>()
    val responseOptions: LiveData<MutableList<String>> = _responseOptions

    private val _progress = MutableLiveData<UiModel>()
    val progress: LiveData<UiModel> = _progress

    private val _navigation = MutableLiveData<Navigation>()
    val navigation: LiveData<Navigation> = _navigation

    private val _showingAds = MutableLiveData<UiModel>()
    val showingAds: LiveData<UiModel> = _showingAds

    init {
        AnalyticsManager.analyticsScreenViewed(AnalyticsManager.SCREEN_GAME)
        generateNewStage()
        _showingAds.value = UiModel.ShowBannerAd(!getPaymentDone())
    }

    fun showRewardedAd() {
        _showingAds.value = UiModel.ShowReewardAd(!getPaymentDone())
    }

    fun generateNewStage() {
        launch {
            _progress.value = UiModel.Loading(true)

            /** Generate question */
            val numRandomMain = generateRandomWithExcusion(0, TOTAL_PRIDES, *randomCountries.toIntArray())
            randomCountries.add(numRandomMain)

            console = getPride(numRandomMain)

            /** Generate responses */
            val numRandomMainPosition = generateRandomWithExcusion(0, 3)

            val numRandomOption1 = generateRandomWithExcusion(0, TOTAL_PRIDES, numRandomMain)
            val option1: Console = getPride(numRandomOption1)
            val numRandomPosition1 = generateRandomWithExcusion(0, 3, numRandomMainPosition)

            val numRandomOption2 = generateRandomWithExcusion(0, TOTAL_PRIDES, numRandomMain, numRandomOption1)
            val option2: Console = getPride(numRandomOption2)
            val numRandomPosition2 = generateRandomWithExcusion(0, 3, numRandomMainPosition, numRandomPosition1)

            val numRandomOption3 = generateRandomWithExcusion(0, TOTAL_PRIDES, numRandomMain, numRandomOption1, numRandomOption2)
            val option3: Console = getPride(numRandomOption3)
            val numRandomPosition3 = generateRandomWithExcusion(0, 3, numRandomMainPosition, numRandomPosition1, numRandomPosition2)

            /** Save value */
            val optionList = mutableListOf("", "", "", "")

            optionList[numRandomMainPosition] = console.name!!
            optionList[numRandomPosition1] = option1.name!!
            optionList[numRandomPosition2] = option2.name!!
            optionList[numRandomPosition3] = option3.name!!

            _responseOptions.value = optionList
            _question.value = console
            _progress.value = UiModel.Loading(false)
        }
    }

    private suspend fun getPride(id: Int): Console {
        return getConsolaById.invoke(id)
    }

    fun navigateToResult(points: String) {
        AnalyticsManager.analyticsGameFinished(points)
        _navigation.value = Navigation.Result
    }

    fun getPride() : Console {
        return console
    }

    fun navigateToExtraLifeDialog() {
        if(!getPaymentDone()) _navigation.value = Navigation.ExtraLifeDialog
        else _navigation.value = Navigation.Result
    }

    private fun generateRandomWithExcusion(start: Int, end: Int, vararg exclude: Int): Int {
        var numRandom = (start..end).random()
        while(exclude.contains(numRandom)){
            numRandom = (start..end).random()
        }
        return numRandom
    }

    sealed class UiModel {
        data class Loading(val show: Boolean) : UiModel()
        data class ShowBannerAd(val show: Boolean) : UiModel()
        data class ShowReewardAd(val show: Boolean) : UiModel()
    }

    sealed class Navigation {
        object Result : Navigation()
        object ExtraLifeDialog : Navigation()
    }
}