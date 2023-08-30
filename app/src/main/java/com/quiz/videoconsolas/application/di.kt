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
import com.quiz.usecases.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun Application.initDI() {
    startKoin {
        androidLogger()
        androidContext(this@initDI)
        modules(appModule, dataModule, scopesModule)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
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
    viewModel { SelectViewModel(get()) }
    viewModel { GameViewModel(get(), get(), get()) }
    viewModel { ResultViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { RankingViewModel(get(), get()) }
    viewModel { InfoViewModel(get(), get()) }
    viewModel { MoreAppsViewModel(get(), get()) }

    factory { GetPaymentDone(get()) }
    factory { GetConsoleById(get()) }
    factory { GetRecordScore(get()) }
    factory { GetAppsRecommended(get()) }
    factory { SaveTopScore(get()) }
    factory { GetPersonalRecord(get())}
    factory { SetPersonalRecord(get())}
    factory { GetRankingScore(get()) }
    factory { GetConsoleList(get()) }
}
