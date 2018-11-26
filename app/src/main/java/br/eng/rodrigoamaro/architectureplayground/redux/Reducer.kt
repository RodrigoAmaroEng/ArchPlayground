package br.eng.rodrigoamaro.architectureplayground.redux

interface Reducer<S : State> {
    fun reduce(action: Action, state: S): S
}