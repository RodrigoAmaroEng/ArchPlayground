package br.eng.rodrigoamaro.architectureplayground

import br.eng.rodrigoamaro.architectureplayground.base.Action
import br.eng.rodrigoamaro.architectureplayground.base.Middleware
import br.eng.rodrigoamaro.architectureplayground.base.Store
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PaymentMiddleware(
        private val service: PaymentService,
        private val launcher: CoLauncher
) : Middleware<SaleState> {
    override fun dispatch(action: Action, store: Store<SaleState>): Action {
        if (action == PayAction)
            GlobalScope.launch {
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