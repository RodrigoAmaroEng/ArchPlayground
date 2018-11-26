package br.eng.rodrigoamaro.architectureplayground.redux

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable

open class SimpleStore<T : State>(
    override val middlewareList: List<Middleware<T>>,
    override val reducerList: List<Reducer<T>>,
    initialState: T
) : Store<T> {
    private val currentState: BehaviorRelay<T> = BehaviorRelay.create()

    init {
        currentState.accept(initialState)
    }

    override fun listen() = currentState

    override fun dispatch(action: Action) {
        Observable.fromIterable(middlewareList)
            .reduce(action) { act, middleware ->
                middleware.dispatch(act, this, currentState.value)
            }
            .flatMap { act ->
                Observable.fromIterable(reducerList)
                    .reduce(currentState.value!!) { state, reducer ->
                        reducer.reduce(act, state)
                    }
            }
            .doOnSuccess { currentState.accept(it) }
            .subscribe()
    }
}