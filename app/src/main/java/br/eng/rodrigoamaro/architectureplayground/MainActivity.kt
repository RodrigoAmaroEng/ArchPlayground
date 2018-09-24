package br.eng.rodrigoamaro.architectureplayground

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView
import br.eng.rodrigoamaro.architectureplayground.base.*
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import java.math.BigDecimal
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {

    private val store = SimpleStore(
            listOf(OperationHandler(), CashHandler(BigDecimal(2.50))),
            AppState("0,00", 0, 'C')
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



/**
 *
 */
data class AppState(
        val amount: String,
        val coffees: Int,
        val operation: Char
) : State

/**
 *
 */
class SimpleInteractor(private val store: Store<AppState>, view: View) : Interactor, Consumer<AppState> {
    private val coffeeCounter: TextView = view.findViewById(R.id.coffee_counter)
    private val coffeeCost: TextView = view.findViewById(R.id.coffee_cost)
    private val decreaseButton: Button = view.findViewById(R.id.button_decrease)
    private val increaseButton: Button = view.findViewById(R.id.button_increase)


    private lateinit var listener: Disposable

    override fun accept(state: AppState) {
        coffeeCounter.text = state.coffees.toString()
        coffeeCost.text = state.amount
    }

    override fun turnOn() {
        listener = store.listenAt(this)
        RxView.clicks(decreaseButton)
                .map { RemoveCoffeeAction }
                .subscribe { store.dispatch(it) }
        RxView.clicks(increaseButton)
                .map { AddCoffeeAction }
                .subscribe { store.dispatch(it) }
    }

    override fun turnOff() {
        listener.dispose()
    }
}

/**
 *
 */
class SimpleStore(
        override val reducerList: List<Reducer<AppState>>,
        override val initialState: AppState
) : Store<AppState> {
    private val currentState: BehaviorRelay<AppState> = BehaviorRelay.create()
init {
    currentState.accept(initialState)
}

    override fun listenAt(observer: Consumer<AppState>): Disposable = currentState.subscribe(observer)

    override fun dispatch(action: Action) {
        var state = currentState.value!!
        reducerList.onEach {
            state = it.reduce(action, state)
        }
        currentState.accept(state)
    }
}


/**
 *
 */
class OperationHandler : Reducer<AppState> {
    override fun reduce(action: Action, state: AppState): AppState {
        val increment = when (action) {
            AddCoffeeAction -> 1
            RemoveCoffeeAction -> -1
            else -> 0
        }
        return state.copy(coffees = state.coffees + increment)
    }
}

/**
 *
 */
class CashHandler(private val coffeePrice: BigDecimal) : Reducer<AppState> {
    override fun reduce(action: Action, state: AppState): AppState {
        val total = coffeePrice.multiply(state.coffees.toBigDecimal())
                .setScale(2, RoundingMode.UP)
        return state.copy(amount = total.toString())
    }

}