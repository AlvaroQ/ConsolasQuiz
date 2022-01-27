package com.quiz.data.repository

import com.quiz.data.datasource.DataBaseSource
import com.quiz.domain.Console

class PrideByIdRepository(private val dataBaseSource: DataBaseSource) {

    suspend fun getConsolaById(id: Int): Console = dataBaseSource.getConsolaById(id)

    suspend fun getConsolaList(currentPage: Int): MutableList<Console> = dataBaseSource.getConsolaList(currentPage)

}