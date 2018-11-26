package br.eng.rodrigoamaro.architectureplayground

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText

class CoffeeBuyerRobot {
    fun addCoffee(): CoffeeBuyerRobot {
        onView(withId(R.id.button_increase)).perform(click())
        return this
    }

    fun finishOrder(): CoffeeBuyerRobot {
        onView(withText("Continuar")).perform(click())
        return this
    }

    fun selectPaymentMethod(method: PaymentMethod): CoffeeBuyerRobot {
        onView(withText(method.text)).perform(click())
        return this
    }

    fun goBack(): CoffeeBuyerRobot {
        Espresso.pressBack()
        return this
    }

    fun check() = SaleResult()

    sealed class PaymentMethod(val text: String) {
        object Debit : PaymentMethod("Débito")
        object Credit : PaymentMethod("Crédito")
        object Voucher : PaymentMethod("Voucher")
    }
}

class SaleResult {
    fun howManyCoffees(number: Int) {
        onView(withText(number.toString())).check(matches(isDisplayed()))
    }

    fun priceIs(price: String) {
        onView(withText("R$ $price")).check(matches(isDisplayed()))
    }

    fun transactionSucceeded() {
        onView(withText("Transação concluída!")).check(matches(isDisplayed()))
    }

    fun transactionNotSucceeded() {
        onView(withText("Transação não completada!")).check(matches(isDisplayed()))
    }

    fun isAbleToEditSale() {
        onView(withId(R.id.button_increase)).check(matches(isEnabled()))
    }
}