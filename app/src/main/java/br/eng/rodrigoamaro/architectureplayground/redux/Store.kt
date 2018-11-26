package br.eng.rodrigoamaro.architectureplayground.redux

import com.jakewharton.rxrelay2.BehaviorRelay

interface Store<S : State> : Dispatcher {
    val middlewareList: List<Middleware<S>>
    val reducerList: List<Reducer<S>>
    fun listen(): BehaviorRelay<S>
}