package br.eng.rodrigoamaro.architectureplayground.coffee

import br.eng.rodrigoamaro.architectureplayground.redux.Action
import br.eng.rodrigoamaro.architectureplayground.redux.Reducer
import br.eng.rodrigoamaro.architectureplayground.redux.ReduxRestoreStateAction

class FlowReducer : Reducer<SaleState> {
    override fun reduce(action: Action, state: SaleState): SaleState {
        return when (action) {
            is ReduxRestoreStateAction<*> -> action.state as SaleState
            is PayAction -> state.copy(status = Status.PROCESSING)
            else -> state
        }
    }
}