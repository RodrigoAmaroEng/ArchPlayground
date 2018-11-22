package br.eng.rodrigoamaro.architectureplayground.coffee

import android.view.View
import android.widget.Button
import br.eng.rodrigoamaro.architectureplayground.Methods
import br.eng.rodrigoamaro.architectureplayground.R
import br.eng.rodrigoamaro.architectureplayground.SimpleInteractor
import br.eng.rodrigoamaro.architectureplayground.base.Store
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable

class PaymentInteractor(store: Store<SaleState>,
                        view: View,
                        private val viewState: ViewState)
    : SimpleInteractor<SaleState>(store) {

    private val buttonDebit = view.findViewById<Button>(R.id.button_debit)
    private val buttonCredit = view.findViewById<Button>(R.id.button_credit)
    private val buttonVoucher = view.findViewById<Button>(R.id.button_voucher)

    init {
        handle(RxView.clicks(buttonDebit)
                .flatMap { Observable.just(PayAction(Methods.DEBIT), NavigateAction(R.id.to_processing_screen)) })
        handle(RxView.clicks(buttonCredit)
                .flatMap { Observable.just(PayAction(Methods.CREDIT), NavigateAction(R.id.to_processing_screen)) })
        handle(RxView.clicks(buttonVoucher)
                .flatMap { Observable.just(PayAction(Methods.VOUCHER), NavigateAction(R.id.to_processing_screen)) })
        viewState.manageState(store)
    }

    override fun accept(state: SaleState?) {
        viewState.currentState = state
    }

}