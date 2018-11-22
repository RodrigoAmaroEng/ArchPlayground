package br.eng.rodrigoamaro.architectureplayground.coffee

import androidx.lifecycle.ViewModel
import br.eng.rodrigoamaro.architectureplayground.base.Store

data class ViewState(var currentState: SaleState? = null) : ViewModel() {
    fun manageState(store: Store<SaleState>) {
        if (currentState == null)
            currentState = store.initialState
        else
            store.dispatch(RestoreStateAction(currentState))
    }
}