package br.eng.rodrigoamaro.architectureplayground

import android.content.SharedPreferences
import java.math.BigDecimal

interface Settings {
    val coffeePrice: Money
}

class SettingsImpl(private val storage: SharedPreferences) : Settings {
    override val coffeePrice: Money
        get() = Money(amount = BigDecimal(storage.getInt("COFFEE_PRICE", 250))
                .setScale(2)
                .divide(BigDecimal(100)))
}