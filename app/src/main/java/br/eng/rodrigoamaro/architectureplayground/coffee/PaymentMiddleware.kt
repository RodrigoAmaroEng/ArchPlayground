package br.eng.rodrigoamaro.architectureplayground.coffee

import br.eng.rodrigoamaro.architectureplayground.CoLauncher
import br.eng.rodrigoamaro.architectureplayground.redux.Action
import br.eng.rodrigoamaro.architectureplayground.redux.Dispatcher
import br.eng.rodrigoamaro.architectureplayground.redux.Middleware

class PaymentMiddleware(
    private val service: PaymentService,
    private val launcher: CoLauncher
) : Middleware<SaleState> {
    override fun dispatch(action: Action, dispatcher: Dispatcher, state: SaleState?): Action {
        if (action is PayAction) {
            launcher.launchThis {
                try {
                    service.pay(state!!)
                    dispatcher.dispatch(PayCompletedAction)
                } catch (ex: Throwable) {
                    dispatcher.dispatch(PayFailedAction)
                }
            }
        }
        return action
    }
}