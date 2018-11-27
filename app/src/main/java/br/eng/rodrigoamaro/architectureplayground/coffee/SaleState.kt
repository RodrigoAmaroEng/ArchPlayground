package br.eng.rodrigoamaro.architectureplayground.coffee

import br.eng.rodrigoamaro.architectureplayground.Money
import br.eng.rodrigoamaro.architectureplayground.money
import br.eng.rodrigoamaro.architectureplayground.redux.State

data class SaleState(
    val amount: Money = money { currency = "R$" },
    val coffees: Int = 0,
    val status: Status = Status.READY_TO_SALE,
    val orderNumber: String? = null
) : State

enum class Status {
    READY_TO_SALE,
    PAYMENT_METHOD,
    PROCESSING,
    FAILED,
    COMPLETED
}
