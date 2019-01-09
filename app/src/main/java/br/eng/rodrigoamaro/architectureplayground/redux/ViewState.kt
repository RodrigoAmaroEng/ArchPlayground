package br.eng.rodrigoamaro.architectureplayground.redux

import androidx.lifecycle.ViewModel

abstract class ViewState<T : State>(var currentState: T? = null) : ViewModel() {
    fun manageState(store: Store<T>) {
        if (currentState != null) {
            store.dispatch(ReduxRestoreStateAction(currentState))
        }
    }
}