package br.eng.rodrigoamaro.architectureplayground

import br.eng.rodrigoamaro.architectureplayground.base.*

class CoffeeCounterReducer : Reducer {
    override fun reduce(action: Action, state: AppState): AppState {
        val coffees = when (action) {
            AddCoffeeAction -> state.coffees + 1
            RemoveCoffeeAction -> state.coffees - 1
            else -> state.coffees
        }
        return state.copy(coffees = coffees)
    }
}