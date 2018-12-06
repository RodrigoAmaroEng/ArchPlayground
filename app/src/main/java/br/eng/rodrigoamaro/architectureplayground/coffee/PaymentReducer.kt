package br.eng.rodrigoamaro.architectureplayground.coffee

import br.eng.rodrigoamaro.architectureplayground.Money
import br.eng.rodrigoamaro.architectureplayground.redux.Action
import br.eng.rodrigoamaro.architectureplayground.redux.Reducer

class PaymentReducer : Reducer<SaleState> {
    override fun reduce(action: Action, state: SaleState): SaleState {
        return when (action) {
            is PayAction -> state.copy(status = Status.PROCESSING)
            is PayCompletedAction -> state.copy(
                status = Status.COMPLETED,
                orderNumber = action.orderNumber
            )
            PayFailedAction -> state.copy(status = Status.FAILED)
            NewSaleAction -> SaleState(Money("R$"), 0)
            UndoSaleAction -> SaleState(Money("R$"), 0)
            else -> state
        }
    }
}