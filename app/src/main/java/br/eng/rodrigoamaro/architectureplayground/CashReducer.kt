package br.eng.rodrigoamaro.architectureplayground

import br.eng.rodrigoamaro.architectureplayground.base.Action
import br.eng.rodrigoamaro.architectureplayground.base.Reducer
import java.math.BigDecimal
import java.math.RoundingMode

class CashReducer(private val coffeePrice: BigDecimal) : Reducer<SaleState> {
    override fun reduce(action: Action, state: SaleState): SaleState {
        val total = coffeePrice.multiply(state.coffees.toBigDecimal())
                .setScale(2, RoundingMode.UP)
        return state.copy(amount = state.amount.copy(amount = total))
    }

}