package br.eng.rodrigoamaro.architectureplayground.redux

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable

open class SimpleStore<T : State>(
    override val middlewareList: List<Middleware<T>>,
    override val reducerList: List<Reducer<T>>,
    initialState: T
) : Store<T> {
    private val currentState: BehaviorRelay<T> = BehaviorRelay.create()
    private val dispatcher = PrivateDispatcher()

    init {
        currentState.accept(initialState)
    }

    override fun listen() = currentState

    override fun dispatch(action: Action) {
        middlewareList.forEach { it.dispatch(action, currentState.value, dispatcher) }
    }

    inner class PrivateDispatcher : Dispatcher {
        override fun dispatch(action: Action) {
            Observable.fromIterable(reducerList)
                .reduce(currentState.value!!) { state, reducer ->
                    reducer.reduce(action, state)
                }
                .doOnSuccess { currentState.accept(it) }
                .subscribe()
        }
    }
}