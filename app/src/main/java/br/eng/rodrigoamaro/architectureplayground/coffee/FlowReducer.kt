package br.eng.rodrigoamaro.architectureplayground.coffee

import br.eng.rodrigoamaro.architectureplayground.base.Action
import br.eng.rodrigoamaro.architectureplayground.base.Reducer

class FlowReducer : Reducer<SaleState> {
    override fun reduce(action: Action, state: SaleState): SaleState {
        return when (action) {
            is RestoreStateAction -> action.state!!
            is PayAction -> state.copy(status = Status.PROCESSING)
            else -> state
        }
    }
}