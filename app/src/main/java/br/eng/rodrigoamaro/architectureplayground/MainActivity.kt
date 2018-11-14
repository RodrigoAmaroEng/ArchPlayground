package br.eng.rodrigoamaro.architectureplayground

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.eng.rodrigoamaro.architectureplayground.base.Interactor
import org.koin.standalone.KoinComponent
import org.koin.standalone.get

class MainActivity : AppCompatActivity(), KoinComponent {

    private val store = PaymentStore(get(), get())
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

class PaymentStore(settings: Settings, service: PaymentService) : SimpleStore<SaleState>(
        listOf(PaymentMiddleware(service)),
        listOf(PaymentReducer(), CounterReducer(), CashReducer(settings.coffeePrice)), SaleState())



