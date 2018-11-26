package br.eng.rodrigoamaro.architectureplayground

import java.math.BigDecimal

data class Money(
    var currency: String = "$",
    var amount: BigDecimal = BigDecimal.ZERO.setScale(2)
) {
    fun value() = "$currency $amount"
}

fun money(block: Money.() -> Unit): Money = Money().apply(block)