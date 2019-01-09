package br.eng.rodrigoamaro.architectureplayground.coffee

import br.eng.rodrigoamaro.architectureplayground.CoLauncher
import br.eng.rodrigoamaro.architectureplayground.redux.Action
import br.eng.rodrigoamaro.architectureplayground.redux.Dispatcher
import br.eng.rodrigoamaro.architectureplayground.redux.Middleware

class PaymentMiddleware(
    private val service: PaymentService,
    private val launcher: CoLauncher
) : Middleware<SaleState> {
    override fun dispatch(action: Action, state: SaleState?, dispatcher: Dispatcher) {
        if (action is PayAction) {
            launcher.launchThis {
                try {
                    val receipt = service.pay(state!!)
                    dispatcher.dispatch(PayCompletedAction(receipt?.orderNumber))
                } catch (ex: Throwable) {
                    ex.printStackTrace()
                    dispatcher.dispatch(PayFailedAction)
                }
            }
        }
        dispatcher.dispatch(action)
    }
}