package br.eng.rodrigoamaro.architectureplayground

import br.eng.rodrigoamaro.architectureplayground.base.Action
import br.eng.rodrigoamaro.architectureplayground.base.Middleware
import br.eng.rodrigoamaro.architectureplayground.base.Reducer
import br.eng.rodrigoamaro.architectureplayground.base.Store
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable

class SimpleStore(
        override val middlewareList: List<Middleware<SaleState>>,
        override val reducerList: List<Reducer<SaleState>>,
        override val initialState: SaleState
) : Store<SaleState> {
    private val currentState: BehaviorRelay<SaleState> = BehaviorRelay.create()

    init {
        currentState.accept(initialState)
    }

    override fun listen() = currentState

    override fun dispatch(action: Action) {
        Observable.fromIterable(middlewareList)
                .reduce(action) { act, middleware -> middleware.dispatch(act, this) }
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