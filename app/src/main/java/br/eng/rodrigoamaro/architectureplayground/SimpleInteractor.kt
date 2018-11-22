package br.eng.rodrigoamaro.architectureplayground

import br.eng.rodrigoamaro.architectureplayground.base.Action
import br.eng.rodrigoamaro.architectureplayground.base.Interactor
import br.eng.rodrigoamaro.architectureplayground.base.State
import br.eng.rodrigoamaro.architectureplayground.base.Store
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