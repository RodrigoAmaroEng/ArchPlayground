package br.eng.rodrigoamaro.architectureplayground.redux

interface Middleware<S : State> {
    fun dispatch(action: Action, state: S?, dispatcher: Dispatcher)
}