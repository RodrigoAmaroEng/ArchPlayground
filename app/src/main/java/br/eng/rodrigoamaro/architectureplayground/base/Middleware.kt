package br.eng.rodrigoamaro.architectureplayground.base

interface Middleware<S : State> {
    fun dispatch(action: Action, store: Store<S>): Action
}