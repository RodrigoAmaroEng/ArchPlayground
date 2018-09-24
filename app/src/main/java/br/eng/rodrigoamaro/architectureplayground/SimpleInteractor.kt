package br.eng.rodrigoamaro.architectureplayground

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import br.eng.rodrigoamaro.architectureplayground.Status.*
import br.eng.rodrigoamaro.architectureplayground.base.Interactor
import br.eng.rodrigoamaro.architectureplayground.base.Store
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

/**
 *
 */
class SimpleInteractor(private val store: Store<SaleState>, view: View) : Interactor, Consumer<SaleState> {
    private val coffeeCounter: TextView = view.findViewById(R.id.coffee_counter)
    private val coffeeCost: TextView = view.findViewById(R.id.coffee_cost)
    private val decreaseButton: Button = view.findViewById(R.id.button_decrease)
    private val increaseButton: Button = view.findViewById(R.id.button_increase)
    private val paymentButton: Button = view.findViewById(R.id.button_pay)
    private val imageStatus: ImageView = view.findViewById(R.id.image_status)


    private lateinit var listener: Disposable

    override fun accept(state: SaleState) {
        coffeeCounter.text = state.coffees.toString()
        coffeeCost.text = state.amount.value()
        paymentButton.isEnabled = state.coffees > 0 && state.status != PROCESSING
        decreaseButton.isEnabled = state.coffees > 0 && state.status == READY_TO_SALE
        increaseButton.isEnabled = state.status == READY_TO_SALE
        imageStatus.visibility = VISIBLE.takeUnless { state.status == READY_TO_SALE } ?: GONE
        when (state.status) {
            READY_TO_SALE -> ready()
            PROCESSING -> processing()
            COMPLETED -> success()
            FAILED -> fail()
        }
    }

    private fun fail() {
        imageStatus.setImageResource(R.drawable.fail)
    }

    private fun success() {
        imageStatus.setImageResource(R.drawable.success)
        paymentButton.text = "Nova venda"
        RxView.clicks(paymentButton)
                .map { NewSaleAction }
                .subscribe { store.dispatch(it) }
    }

    private fun processing() {
        imageStatus.setImageResource(R.drawable.loading)

    }

    private fun ready() {
        paymentButton.text = "Pagar"
        RxView.clicks(paymentButton)
                .map { PayAction }
                .subscribe { store.dispatch(it) }
    }

    override fun turnOn() {
        listener = store.listen()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this)
        RxView.clicks(decreaseButton)
                .map { RemoveCoffeeAction }
                .subscribe { store.dispatch(it) }
        RxView.clicks(increaseButton)
                .map { AddCoffeeAction }
                .subscribe { store.dispatch(it) }
        RxView.clicks(paymentButton)
                .map { PayAction }
                .subscribe { store.dispatch(it) }
    }

    override fun turnOff() {
        listener.dispose()
    }
}