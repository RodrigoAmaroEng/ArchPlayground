package br.eng.rodrigoamaro.architectureplayground.base

interface Reducer<S : State> {
    fun reduce(action: Action, state: S): S
}