package br.eng.rodrigoamaro.architectureplayground

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import br.eng.rodrigoamaro.architectureplayground.Status.*
import br.eng.rodrigoamaro.architectureplayground.base.Action
import br.eng.rodrigoamaro.architectureplayground.base.Interactor
import br.eng.rodrigoamaro.architectureplayground.base.Store
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
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
    private val textStatus: TextView = view.findViewById(R.id.text_status)
    private val eventDisposal = CompositeDisposable()


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
        textStatus.setText(R.string.transaction_failed)
        imageStatus.setImageResource(R.drawable.fail)
    }

    private fun success() {
        textStatus.setText(R.string.transaction_suceeded)
        imageStatus.setImageResource(R.drawable.success)
        paymentButton.setText(R.string.action_new_sale)
        handle(RxView.clicks(paymentButton).map { NewSaleAction })
    }

    private fun processing() {
        imageStatus.setImageResource(R.drawable.loading)

    }

    private fun ready() {
        textStatus.text = ""
        paymentButton.setText(R.string.action_pay)
        handle(RxView.clicks(paymentButton).map { PayAction })
    }

    override fun turnOn() {
        eventDisposal.add(store.listen()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this))
        handle(RxView.clicks(decreaseButton).map { RemoveCoffeeAction })
        handle(RxView.clicks(increaseButton).map { AddCoffeeAction })
        handle(RxView.clicks(paymentButton).map { PayAction })
    }

    override fun turnOff() {
        eventDisposal.dispose()
    }

    private fun handle(action: Observable<Action>) {
        eventDisposal.add(action.subscribe { store.dispatch(it) })
    }
}