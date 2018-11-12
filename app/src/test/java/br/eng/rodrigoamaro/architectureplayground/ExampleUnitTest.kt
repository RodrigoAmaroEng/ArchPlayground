package br.eng.rodrigoamaro.architectureplayground

import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext


@RunWith(AndroidJUnit4::class)
class Fixtures {

    private val mockedPaymentService = object : PaymentService {
        override suspend fun pay(invoice: SaleState) {
        }
    }

    @Test
    fun tryToBuySomeCoffee() {
        StandAloneContext.getKoin().loadModules(listOf(
                module(override = true) { single<PaymentService> { mockedPaymentService } }
        ))
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.button_increase)).perform(ViewActions.click())
        onView(withText("1")).check(isVisible())
        onView(withText("Pagar")).perform(ViewActions.click())
        onView(withText("Transação concluída!")).check(isVisible())

    }

    private fun isVisible() = ViewAssertion { view, _ -> assertThat(view, VisibilityMatcher(View.VISIBLE)) }

}

private class VisibilityMatcher(private val visibility: Int) : BaseMatcher<View>() {

    override fun describeTo(description: Description) {
        val visibilityName: String = when (visibility) {
            View.GONE -> "GONE"
            View.VISIBLE -> "VISIBLE"
            else -> "INVISIBLE"
        }
        description.appendText("View visibility must has equals $visibilityName")
    }

    override fun matches(o: Any?): Boolean {
        if (o == null) {
            if (visibility == View.GONE || visibility == View.INVISIBLE)
                return true
            else if (visibility == View.VISIBLE) return false
        }

        if (o !is View)
            throw IllegalArgumentException("Object must be instance of View. Object is instance of " + o!!)
        return o.visibility == visibility
    }
}