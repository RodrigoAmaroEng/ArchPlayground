package br.eng.rodrigoamaro.architectureplayground.coffee

import br.eng.rodrigoamaro.architectureplayground.Money
import br.eng.rodrigoamaro.architectureplayground.base.Action
import br.eng.rodrigoamaro.architectureplayground.base.Reducer

class PaymentReducer : Reducer<SaleState> {
    override fun reduce(action: Action, state: SaleState): SaleState {
        return when (action) {
            is PayAction -> state.copy(status = Status.PROCESSING)
            PayCompletedAction -> state.copy(status = Status.COMPLETED)
            PayFailedAction -> state.copy(status = Status.FAILED)
            NewSaleAction -> SaleState(Money("R$"), 0)
            else -> state
        }
    }
}