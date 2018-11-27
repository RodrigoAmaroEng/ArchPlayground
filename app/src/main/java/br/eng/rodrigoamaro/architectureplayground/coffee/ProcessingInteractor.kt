package br.eng.rodrigoamaro.architectureplayground.coffee

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import br.eng.rodrigoamaro.architectureplayground.R
import br.eng.rodrigoamaro.architectureplayground.redux.SimpleInteractor
import br.eng.rodrigoamaro.architectureplayground.redux.Store

class ProcessingInteractor(store: Store<SaleState>, view: View, private val viewState: ViewState) :
    SimpleInteractor<SaleState>(store) {

    private val imageStatus: ImageView = view.findViewById(R.id.image_status)
    private val textStatus: TextView = view.findViewById(R.id.text_status)

    init {
        imageStatus.setImageResource(R.drawable.loading)
        viewState.manageState(store)
    }

    override fun accept(state: SaleState) {
        viewState.currentState = state
        if (state.status == Status.COMPLETED)
            success(state.orderNumber)
        else if (state.status == Status.FAILED)
            fail()
    }

    private fun fail() {
        textStatus.setText(R.string.transaction_failed)
        imageStatus.setImageResource(R.drawable.fail)
    }

    private fun success(orderNumber: String?) {
        val text = imageStatus.resources.getText(R.string.transaction_suceeded).toString()
        textStatus.text = String.format(text, orderNumber)
        imageStatus.setImageResource(R.drawable.success)
    }
}
