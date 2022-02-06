package com.quiz.videoconsolas.application

import android.app.Application
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.quiz.data.datasource.DataBaseSource
import com.quiz.data.datasource.FirestoreDataSource
import com.quiz.data.datasource.SharedPreferencesLocalDataSource
import com.quiz.data.repository.AppsRecommendedRepository
import com.quiz.data.repository.ConsoleByIdRepository
import com.quiz.data.repository.RankingRepository
import com.quiz.data.repository.SharedPreferencesRepository
import com.quiz.videoconsolas.common.ResourceProvider
import com.quiz.videoconsolas.common.ResourceProviderImpl
import com.quiz.videoconsolas.datasource.DataBaseSourceImpl
import com.quiz.videoconsolas.datasource.FirestoreDataSourceImpl
import com.quiz.videoconsolas.managers.SharedPrefsDataSource
import com.quiz.videoconsolas.ui.game.GameFragment
import com.quiz.videoconsolas.ui.game.GameViewModel
import com.quiz.videoconsolas.ui.info.InfoFragment
import com.quiz.videoconsolas.ui.info.InfoViewModel
import com.quiz.videoconsolas.ui.moreApps.MoreAppsFragment
import com.quiz.videoconsolas.ui.moreApps.MoreAppsViewModel
import com.quiz.videoconsolas.ui.ranking.RankingFragment
import com.quiz.videoconsolas.ui.ranking.RankingViewModel
import com.quiz.videoconsolas.ui.result.ResultFragment
import com.quiz.videoconsolas.ui.result.ResultViewModel
import com.quiz.videoconsolas.ui.select.SelectFragment
import com.quiz.videoconsolas.ui.select.SelectViewModel
import com.quiz.videoconsolas.ui.settings.SettingsFragment
import com.quiz.videoconsolas.ui.settings.SettingsViewModel
import com.quiz.usecases.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun Application.initDI() {
    startKoin {
        androidLogger()
        androidContext(this@initDI)
        koin.loadModules(listOf(
            appModule,
            dataModule,
            scopesModule
        ))
        koin.createRootScope()
    }
}

private val appModule = module {
    factory<ResourceProvider> { ResourceProviderImpl(androidContext().resources) }
    factory { Firebase.firestore }
    single<CoroutineDispatcher> { Dispatchers.Main }
    factory<DataBaseSource> { DataBaseSourceImpl() }
    factory<FirestoreDataSource> { FirestoreDataSourceImpl(get()) }
    factory<SharedPreferencesLocalDataSource> { SharedPrefsDataSource(get()) }
}

val dataModule = module {
    factory { ConsoleByIdRepository(get()) }
    factory { AppsRecommendedRepository(get()) }
    factory { RankingRepository(get()) }
    factory { SharedPreferencesRepository(get()) }
}

private val scopesModule = module {
    scope(named<SelectFragment>()) {
        viewModel { SelectViewModel(get()) }
        scoped { GetPaymentDone(get()) }
    }
    scope(named<GameFragment>()) {
        viewModel { GameViewModel(get(), get(), get()) }
        scoped { GetConsoleById(get()) }
        scoped { GetPaymentDone(get()) }
    }
    scope(named<ResultFragment>()) {
        viewModel { ResultViewModel(get(), get(), get(), get(), get(), get()) }
        scoped { GetRecordScore(get()) }
        scoped { GetAppsRecommended(get()) }
        scoped { SaveTopScore(get()) }
        scoped { GetPersonalRecord(get())}
        scoped { SetPersonalRecord(get())}
        scoped { GetPaymentDone(get()) }
    }
    scope(named<RankingFragment>()) {
        viewModel { RankingViewModel(get(), get()) }
        scoped { GetRankingScore(get()) }
        scoped { GetPaymentDone(get()) }
    }
    scope(named<InfoFragment>()) {
        viewModel { InfoViewModel(get(), get()) }
        scoped { GetConsoleList(get()) }
        scoped { GetPaymentDone(get()) }
    }
    scope(named<MoreAppsFragment>()) {
        viewModel { MoreAppsViewModel(get(), get()) }
        scoped { GetAppsRecommended(get()) }
        scoped { GetPaymentDone(get()) }
    }
    scope(named<SettingsFragment>()) {
        viewModel { SettingsViewModel(get(), get()) }
        scoped { GetPaymentDone(get()) }
        scoped { SetPaymentDone(get()) }
    }
}
