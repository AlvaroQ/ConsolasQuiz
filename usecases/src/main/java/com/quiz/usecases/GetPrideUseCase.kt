package com.quiz.usecases

import com.quiz.data.repository.PrideByIdRepository
import com.quiz.domain.Console


class GetConsolaById(private val prideByIdRepository: PrideByIdRepository) {
    suspend fun invoke(id: Int): Console = prideByIdRepository.getConsolaById(id)
}

class GetConsolaList(private val prideByIdRepository: PrideByIdRepository) {
    suspend fun invoke(currentPage: Int): MutableList<Console> = prideByIdRepository.getConsolaList(currentPage)
}
