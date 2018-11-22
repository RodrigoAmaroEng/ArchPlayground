package br.eng.rodrigoamaro.architectureplayground.coffee

import android.view.View
import android.widget.Button
import androidx.navigation.NavController
import br.eng.rodrigoamaro.architectureplayground.Methods
import br.eng.rodrigoamaro.architectureplayground.R
import br.eng.rodrigoamaro.architectureplayground.SimpleInteractor
import br.eng.rodrigoamaro.architectureplayground.base.Store
import com.jakewharton.rxbinding2.view.RxView

class PaymentInteractor(store: Store<SaleState>, view: View, private val navigator: NavController)
    : SimpleInteractor<SaleState>(store) {

    private val buttonDebit = view.findViewById<Button>(R.id.button_debit)
    private val buttonCredit = view.findViewById<Button>(R.id.button_credit)
    private val buttonVoucher = view.findViewById<Button>(R.id.button_voucher)

    init {
        handle(RxView.clicks(buttonDebit).map { PayAction(Methods.DEBIT) })
        handle(RxView.clicks(buttonCredit).map { PayAction(Methods.CREDIT) })
        handle(RxView.clicks(buttonVoucher).map { PayAction(Methods.VOUCHER) })
    }

    override fun accept(state: SaleState?) {
        if (state?.status == Status.PROCESSING) {
            navigator.navigate(R.id.to_processing_screen)
        }
    }

}