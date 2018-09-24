package br.eng.rodrigoamaro.architectureplayground.base

import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

interface Store<S : State> {
    val reducerList :List<Reducer<S>>
    val initialState: S
    fun dispatch(action: Action)
    fun listenAt(observer: Consumer<S>): Disposable
}