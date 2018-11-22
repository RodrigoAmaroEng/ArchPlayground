package br.eng.rodrigoamaro.architectureplayground.coffee

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import br.eng.rodrigoamaro.architectureplayground.R
import br.eng.rodrigoamaro.architectureplayground.SimpleInteractor
import br.eng.rodrigoamaro.architectureplayground.base.Store

class ProcessingInteractor(store: Store<SaleState>, view: View, private val viewState: ViewState)
    : SimpleInteractor<SaleState>(store) {

    private val imageStatus: ImageView = view.findViewById(R.id.image_status)
    private val textStatus: TextView = view.findViewById(R.id.text_status)

    init {
        imageStatus.setImageResource(R.drawable.loading)
        viewState.manageState(store)
    }

    override fun accept(state: SaleState) {
        viewState.currentState = state
        if (state.status == Status.COMPLETED)
            success()
        else if (state.status == Status.FAILED)
            fail()

    }

    private fun fail() {
        textStatus.setText(R.string.transaction_failed)
        imageStatus.setImageResource(R.drawable.fail)
    }

    private fun success() {
        textStatus.setText(R.string.transaction_suceeded)
        imageStatus.setImageResource(R.drawable.success)
    }

}
