package br.eng.rodrigoamaro.architectureplayground.base

import br.eng.rodrigoamaro.architectureplayground.base.Action
import br.eng.rodrigoamaro.architectureplayground.base.AppState

interface Reducer {
    fun reduce(action: Action, state: AppState): AppState
}