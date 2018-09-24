package br.eng.rodrigoamaro.architectureplayground

import br.eng.rodrigoamaro.architectureplayground.base.Action
import br.eng.rodrigoamaro.architectureplayground.base.Middleware
import br.eng.rodrigoamaro.architectureplayground.base.Store
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
class PaymentMiddleware : Middleware<SaleState> {
    override fun dispatch(action: Action, store: Store<SaleState>): Action {
        if (action == PayAction)
            launch {
                delay(3000)
                store.dispatch(PayCompletedAction)
            }
        return action
    }
}