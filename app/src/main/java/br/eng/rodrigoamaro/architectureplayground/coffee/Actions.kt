package br.eng.rodrigoamaro.architectureplayground.coffee

import br.eng.rodrigoamaro.architectureplayground.Methods
import br.eng.rodrigoamaro.architectureplayground.base.Action

object AddCoffeeAction : Action()
object RemoveCoffeeAction : Action()

object BackPressed : Action()

object SetCoffeesAction : Action()

data class PayAction(val method: Methods) : Action()
object PayCompletedAction : Action()
object PayFailedAction : Action()

object NewSaleAction : Action()