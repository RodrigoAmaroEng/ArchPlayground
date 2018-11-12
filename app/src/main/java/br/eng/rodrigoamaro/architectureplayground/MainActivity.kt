package br.eng.rodrigoamaro.architectureplayground

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.eng.rodrigoamaro.architectureplayground.base.Interactor
import org.koin.standalone.StandAloneContext
import java.math.BigDecimal

class MainActivity : AppCompatActivity() {

    private val store = PaymentStore()

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

class PaymentStore : SimpleStore<SaleState>(
        listOf(PaymentMiddleware(StandAloneContext.getKoin().koinContext.get())),
        listOf(PaymentReducer(), CounterReducer(), CashReducer(BigDecimal(2.50))),
        SaleState(money { currency = "R$" }, 0))




