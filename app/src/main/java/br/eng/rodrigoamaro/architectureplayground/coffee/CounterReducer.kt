package br.eng.rodrigoamaro.architectureplayground.coffee

import br.eng.rodrigoamaro.architectureplayground.redux.Action
import br.eng.rodrigoamaro.architectureplayground.redux.Reducer

class CounterReducer : Reducer<SaleState> {
    override fun reduce(action: Action, state: SaleState): SaleState {
        val increment = when (action) {
            AddCoffeeAction -> 1
            RemoveCoffeeAction -> -1
            else -> 0
        }
        return state.copy(coffees = state.coffees + increment)
    }
}