package br.eng.rodrigoamaro.architectureplayground.coffee

import android.view.View
import android.widget.Button
import android.widget.TextView
import br.eng.rodrigoamaro.architectureplayground.R
import br.eng.rodrigoamaro.architectureplayground.SimpleInteractor
import br.eng.rodrigoamaro.architectureplayground.base.Store
import com.jakewharton.rxbinding2.view.RxView

class CoffeeInteractor(
        store: Store<SaleState>,
        view: View,
        private val viewState: ViewState)
    : SimpleInteractor<SaleState>(store) {

    private val coffeeCounter: TextView = view.findViewById(R.id.coffee_counter)
    private val coffeeCost: TextView = view.findViewById(R.id.coffee_cost)
    private val decreaseButton: Button = view.findViewById(R.id.button_decrease)
    private val increaseButton: Button = view.findViewById(R.id.button_increase)
    private val paymentButton: Button = view.findViewById(R.id.button_pay)

    init {
        handle(RxView.clicks(decreaseButton).map { RemoveCoffeeAction })
        handle(RxView.clicks(increaseButton).map { AddCoffeeAction })
        handle(RxView.clicks(paymentButton).map { NavigateAction(R.id.to_payment_method_screen) })
        viewState.manageState(store)
    }

    override fun accept(state: SaleState) {
        viewState.currentState = state
        coffeeCounter.text = state.coffees.toString()
        coffeeCost.text = state.amount.value()
        paymentButton.isEnabled = state.coffees > 0 && state.status != Status.PROCESSING
        decreaseButton.isEnabled = state.coffees > 0 && state.status == Status.READY_TO_SALE
        increaseButton.isEnabled = state.status == Status.READY_TO_SALE
    }

}