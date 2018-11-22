package br.eng.rodrigoamaro.architectureplayground.coffee

import androidx.navigation.fragment.NavHostFragment
import br.eng.rodrigoamaro.architectureplayground.base.Action
import br.eng.rodrigoamaro.architectureplayground.base.Middleware
import br.eng.rodrigoamaro.architectureplayground.base.Store

class NavigatorMiddleware(private val navigator: NavHostFragment) : Middleware<SaleState> {
    override fun dispatch(action: Action, store: Store<SaleState>): Action {
        if (action is NavigateAction)
            navigator.navController.navigate(action.destination)
        return action
    }
}