package br.eng.rodrigoamaro.architectureplayground.base

import com.jakewharton.rxrelay2.BehaviorRelay

interface Store<S : State> {
    val middlewareList: List<Middleware<S>>
    val reducerList: List<Reducer<S>>
    val initialState: S
    fun dispatch(action: Action)
    fun listen(): BehaviorRelay<S>
}