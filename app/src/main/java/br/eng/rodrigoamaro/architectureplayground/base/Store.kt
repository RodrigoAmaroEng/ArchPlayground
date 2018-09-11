package br.eng.rodrigoamaro.architectureplayground.base

interface Store<State : AppState> {
    val currentState: AppState
    fun dispatch(action: Action)
}