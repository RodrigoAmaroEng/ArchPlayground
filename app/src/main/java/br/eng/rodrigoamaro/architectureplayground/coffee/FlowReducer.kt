package br.eng.rodrigoamaro.architectureplayground.coffee

import br.eng.rodrigoamaro.architectureplayground.base.Action
import br.eng.rodrigoamaro.architectureplayground.base.Reducer

class FlowReducer : Reducer<SaleState> {
    override fun reduce(action: Action, state: SaleState): SaleState {
        val last = if (state.status == Status.PROCESSING) Status.PAYMENT_METHOD else Status.READY_TO_SALE
        return when (action) {
            BackPressed -> state.copy(status = last)
            SetCoffeesAction -> state.copy(status = Status.PAYMENT_METHOD)
            is PayAction -> state.copy(status = Status.PROCESSING)
            else -> state
        }
    }
}