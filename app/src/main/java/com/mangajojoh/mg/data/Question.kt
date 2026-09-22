package com.mangajojoh.mg.data

data class Question(
    val id: Int,
    val theme: String,
    val enonce: String,
    val choix: List<String>,
    val bonneReponse: Int,
    val difficulte: Int
)
