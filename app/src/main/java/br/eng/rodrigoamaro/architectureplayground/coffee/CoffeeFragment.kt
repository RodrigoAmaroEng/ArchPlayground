package br.eng.rodrigoamaro.architectureplayground.coffee

import android.view.View
import br.eng.rodrigoamaro.architectureplayground.R
import br.eng.rodrigoamaro.architectureplayground.android.BaseFragment
import br.eng.rodrigoamaro.architectureplayground.redux.Interactor
import br.eng.rodrigoamaro.architectureplayground.redux.Store

class CoffeeFragment : BaseFragment<SaleState, ViewState>() {
    override val modelClass: Class<ViewState> = ViewState::class.java

    override val layoutResId: Int = R.layout.fragment_coffees

    override fun createInteractor(store: Store<SaleState>, view: View): Interactor =
        CoffeeInteractor(store, view, model)
}