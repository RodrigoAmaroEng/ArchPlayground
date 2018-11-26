package br.eng.rodrigoamaro.architectureplayground.android

import androidx.appcompat.app.AppCompatActivity
import br.eng.rodrigoamaro.architectureplayground.redux.State
import br.eng.rodrigoamaro.architectureplayground.redux.Store

abstract class BaseActivity<S : State> : AppCompatActivity() {
    lateinit var store: Store<S>
}