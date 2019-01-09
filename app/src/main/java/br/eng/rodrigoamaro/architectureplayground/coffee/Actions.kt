package br.eng.rodrigoamaro.architectureplayground.coffee

import androidx.annotation.IdRes
import br.eng.rodrigoamaro.architectureplayground.Methods
import br.eng.rodrigoamaro.architectureplayground.redux.Action

object AddCoffeeAction : Action()
object RemoveCoffeeAction : Action()

data class NavigateAction(@IdRes val destination: Int) : Action()

data class PayAction(val method: Methods) : Action()
data class PayCompletedAction(val orderNumber: String?) : Action()
object PayFailedAction : Action()

object NewSaleAction : Action()

object UndoSaleAction : Action()