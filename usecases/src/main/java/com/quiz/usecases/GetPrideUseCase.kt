package com.quiz.usecases

import com.quiz.data.repository.ConsoleByIdRepository
import com.quiz.domain.Console


class GetConsoleById(private val consoleByIdRepository: ConsoleByIdRepository) {
    suspend fun invoke(id: Int): Console = consoleByIdRepository.getConsolaById(id)
}

class GetConsoleList(private val consoleByIdRepository: ConsoleByIdRepository) {
    suspend fun invoke(currentPage: Int): MutableList<Console> = consoleByIdRepository.getConsolaList(currentPage)
}
