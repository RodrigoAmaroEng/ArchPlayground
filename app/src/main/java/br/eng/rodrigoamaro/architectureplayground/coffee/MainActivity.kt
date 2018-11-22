package br.eng.rodrigoamaro.architectureplayground.coffee

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import br.eng.rodrigoamaro.architectureplayground.CoLauncher
import br.eng.rodrigoamaro.architectureplayground.R
import br.eng.rodrigoamaro.architectureplayground.Settings
import br.eng.rodrigoamaro.architectureplayground.SimpleStore
import org.koin.standalone.KoinComponent
import org.koin.standalone.get

class MainActivity : AppCompatActivity(), KoinComponent {

    private val navigator = NavHostFragment.create(R.navigation.navigation_flow)

    lateinit var store: PaymentStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        store = PaymentStore(get(), get(), get(), navigator)
        supportFragmentManager.beginTransaction()
                .replace(R.id.my_nav_host_fragment, navigator)
                .setPrimaryNavigationFragment(navigator) // this is the equivalent to app:defaultNavHost="true"
                .commit()
    }


}

class PaymentStore(settings: Settings, service: PaymentService, launcher: CoLauncher, navHost: NavHostFragment) : SimpleStore<SaleState>(
        listOf(PaymentMiddleware(service, launcher), NavigatorMiddleware(navHost)),
        listOf(PaymentReducer(), CounterReducer(), CashReducer(settings.coffeePrice), FlowReducer()), SaleState())



