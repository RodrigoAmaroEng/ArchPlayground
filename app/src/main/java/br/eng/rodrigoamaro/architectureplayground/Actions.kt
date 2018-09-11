package br.eng.rodrigoamaro.architectureplayground

import br.eng.rodrigoamaro.architectureplayground.base.Action

data class PaymentTypeSetAction(val type: Char) : Action()

object AddCoffeeAction : Action()

object RemoveCoffeeAction : Action()