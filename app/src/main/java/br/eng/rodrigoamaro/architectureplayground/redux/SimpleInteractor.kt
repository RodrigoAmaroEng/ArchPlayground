package br.eng.rodrigoamaro.architectureplayground.redux

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer

/**
 *
 */
abstract class SimpleInteractor<STATE : State>(private val store: Store<STATE>) : Interactor, Consumer<STATE> {
    private val eventDisposal = CompositeDisposable()

    override fun turnOn() {
        eventDisposal.add(store.listen()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this))
    }

    override fun turnOff() {
        eventDisposal.dispose()
    }

    fun handle(action: Observable<Action>) {
        eventDisposal.add(action.subscribe {
            store.dispatch(it)
        })
    }
}