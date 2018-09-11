package br.eng.rodrigoamaro.architectureplayground.base

data class AppState(
        val amount: String,
        val coffees: Int,
        val operation: Char
)