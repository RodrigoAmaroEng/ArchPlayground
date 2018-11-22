package br.eng.rodrigoamaro.architectureplayground.coffee

import br.eng.rodrigoamaro.architectureplayground.base.Action
import br.eng.rodrigoamaro.architectureplayground.base.Reducer

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