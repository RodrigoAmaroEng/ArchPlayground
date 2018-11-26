package br.eng.rodrigoamaro.architectureplayground.coffee

import androidx.navigation.fragment.NavHostFragment
import br.eng.rodrigoamaro.architectureplayground.redux.Action
import br.eng.rodrigoamaro.architectureplayground.redux.Dispatcher
import br.eng.rodrigoamaro.architectureplayground.redux.Middleware

class NavigatorMiddleware(private val navigator: NavHostFragment) : Middleware<SaleState> {
    override fun dispatch(action: Action, dispatcher: Dispatcher, state: SaleState?): Action {
        if (action is NavigateAction)
            navigator.navController.navigate(action.destination)
        return action
    }
}