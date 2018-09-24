package br.eng.rodrigoamaro.architectureplayground

import br.eng.rodrigoamaro.architectureplayground.base.State

data class SaleState(
        val amount: Money,
        val coffees: Int,
        val status: Status = Status.READY_TO_SALE
) : State

enum class Status {
    READY_TO_SALE,
    PROCESSING,
    FAILED,
    COMPLETED
}
