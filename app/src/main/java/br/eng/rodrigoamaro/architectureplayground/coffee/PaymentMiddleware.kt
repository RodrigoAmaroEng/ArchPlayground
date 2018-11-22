package br.eng.rodrigoamaro.architectureplayground.coffee

import br.eng.rodrigoamaro.architectureplayground.CoLauncher
import br.eng.rodrigoamaro.architectureplayground.base.Action
import br.eng.rodrigoamaro.architectureplayground.base.Middleware
import br.eng.rodrigoamaro.architectureplayground.base.Store

class PaymentMiddleware(
        private val service: PaymentService,
        private val launcher: CoLauncher
) : Middleware<SaleState> {
    override fun dispatch(action: Action, store: Store<SaleState>): Action {
        if (action is PayAction)
            launcher.launchThis {
                try {
                    service.pay(store.initialState)
                    store.dispatch(PayCompletedAction)
                } catch (ex: Throwable) {
                    store.dispatch(PayFailedAction)
                }
            }
        return action
    }
}