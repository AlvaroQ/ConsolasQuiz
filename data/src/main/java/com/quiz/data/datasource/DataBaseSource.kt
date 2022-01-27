package com.quiz.data.datasource

import com.quiz.domain.App
import com.quiz.domain.Console

interface DataBaseSource {
    suspend fun getConsolaById(id: Int): Console
    suspend fun getConsolaList(currentPage: Int): MutableList<Console>
    suspend fun getAppsRecommended(): MutableList<App>
}