package com.quiz.domain

data class Console(
    var description: Name? = null,
    var company: String? = "",
    var image: String? = "",
    var name: String? = "",
    var sold: Int? = 0,
    var year: Int? = 0,
    var generation: Int? = 1,
)