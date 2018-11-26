package br.eng.rodrigoamaro.architectureplayground.redux

interface Middleware<S : State> {
    fun dispatch(action: Action, dispatcher: Dispatcher, state: S?): Action
}