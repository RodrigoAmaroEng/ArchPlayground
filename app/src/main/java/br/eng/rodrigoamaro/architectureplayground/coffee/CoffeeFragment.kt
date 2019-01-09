package br.eng.rodrigoamaro.architectureplayground.coffee

import android.view.View
import br.eng.rodrigoamaro.architectureplayground.R
import br.eng.rodrigoamaro.architectureplayground.android.BaseFragment
import br.eng.rodrigoamaro.architectureplayground.redux.Interactor
import br.eng.rodrigoamaro.architectureplayground.redux.Store

class CoffeeFragment : BaseFragment<SaleState, SaleViewState>() {
    override val modelClass: Class<SaleViewState> = SaleViewState::class.java

    override val layoutResId: Int = R.layout.fragment_coffees

    override fun createInteractor(store: Store<SaleState>, view: View): Interactor =
        CoffeeInteractor(store, view, model)
}