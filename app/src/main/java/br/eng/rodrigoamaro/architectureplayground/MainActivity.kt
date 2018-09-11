package br.eng.rodrigoamaro.architectureplayground

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import br.eng.rodrigoamaro.architectureplayground.base.Action
import br.eng.rodrigoamaro.architectureplayground.base.AppState
import br.eng.rodrigoamaro.architectureplayground.base.Interactor
import br.eng.rodrigoamaro.architectureplayground.base.Store

class MainActivity : AppCompatActivity() {

    private val store = SimpleStore(AppState("0,00", 1, 'C'))
    private lateinit var interactor :Interactor

    init {
        lifecycle.addObserver(interactor)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        interactor = SimpleInteractor(store, window.decorView)
    }
}

class SimpleInteractor(private val store: Store<AppState>, view: View) : Interactor {


    override fun turnOn() {
        println("MyLifecycle ON")
    }

    override fun turnOff() {
        println("MyLifecycle OFF")
    }
}

class SimpleStore(override val currentState: AppState) : Store<AppState> {
    override fun dispatch(action: Action) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
