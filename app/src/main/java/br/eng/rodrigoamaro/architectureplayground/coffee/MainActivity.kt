package br.eng.rodrigoamaro.architectureplayground.coffee

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import br.eng.rodrigoamaro.architectureplayground.CoLauncher
import br.eng.rodrigoamaro.architectureplayground.R
import br.eng.rodrigoamaro.architectureplayground.Settings
import br.eng.rodrigoamaro.architectureplayground.android.BaseActivity
import br.eng.rodrigoamaro.architectureplayground.redux.SimpleStore
import br.eng.rodrigoamaro.architectureplayground.redux.ViewState
import org.koin.standalone.KoinComponent
import org.koin.standalone.get

class MainActivity : BaseActivity<SaleState>(), KoinComponent {

    private val navigator = NavHostFragment.create(R.navigation.navigation_flow)


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

class SaleViewState : ViewState<SaleState>()

class PaymentStore(
    settings: Settings,
    service: PaymentService,
    launcher: CoLauncher,
    navHost: NavHostFragment
) : SimpleStore<SaleState>(
    listOf(PaymentMiddleware(service, launcher), NavigatorMiddleware(navHost)),
    listOf(PaymentReducer(), CounterReducer(), CashReducer(settings.coffeePrice), FlowReducer()),
    SaleState()
)