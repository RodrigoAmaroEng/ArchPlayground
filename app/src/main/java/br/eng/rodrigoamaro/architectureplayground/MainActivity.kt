package br.eng.rodrigoamaro.architectureplayground

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.eng.rodrigoamaro.architectureplayground.base.Interactor
import java.math.BigDecimal

class MainActivity : AppCompatActivity() {

    private val store = SimpleStore(
            listOf(PaymentMiddleware()),
            listOf(PaymentReducer(), CounterReducer(), CashReducer(BigDecimal(2.50))),
            SaleState(money { currency = "R$" }, 0)
    )

    private lateinit var interactor: Interactor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        interactor = SimpleInteractor(store, window.decorView)
        interactor.turnOn()
    }

    override fun onDestroy() {
        interactor.turnOff()
        super.onDestroy()
    }
}



