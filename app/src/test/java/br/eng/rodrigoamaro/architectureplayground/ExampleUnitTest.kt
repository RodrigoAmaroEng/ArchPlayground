package br.eng.rodrigoamaro.architectureplayground

import android.preference.PreferenceManager
import android.view.View
import androidx.test.InstrumentationRegistry
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext
import java.util.concurrent.atomic.AtomicBoolean


@RunWith(AndroidJUnit4::class)
class Fixtures {
    private val server = MockWebServer()
    private val mockedPaymentService: PaymentService = mockk()

    @Before
    fun setUp() {
        coEvery { mockedPaymentService.pay(any()) } returns Unit
        StandAloneContext.getKoin().loadModules(listOf(
                module {
                    single(override = true) { mockedPaymentService }
                    single<CoLauncher>(override = true) { TestCoLauncher() }
                }
        ))
        IdlingRegistry.getInstance().register(CoroutineIdlingResource.default)
        server.start(8080)
    }

    @Test
    fun tryToBuySomeCoffee() {
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.button_increase)).perform(ViewActions.click())
        onView(withText("1")).check(isVisible())
        onView(withText("Pagar")).perform(ViewActions.click())
        onView(withText("Transação concluída!")).check(isVisible())
    }

    @Test
    fun newCoffeePrice() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(InstrumentationRegistry.getTargetContext())
        preferences.edit().putInt("COFFEE_PRICE", 150).apply()
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.button_increase)).perform(ViewActions.click())
        onView(withId(R.id.button_increase)).perform(ViewActions.click())
        onView(withText("R$ 3.00")).check(isVisible())
    }

    @Test
    fun checkRealServiceCommunication() {
        StandAloneContext.getKoin().loadModules(listOf(
                module { single<PaymentService>(override = true) { PaymentServiceImpl(get()) } }
        ))
        server.enqueue(MockResponse().setResponseCode(200).setBody("{\"orderNumber\":\"1234\"}"))
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.button_increase)).perform(ViewActions.click())
        onView(withText("Pagar")).perform(ViewActions.click())
        onView(withText("Transação concluída!")).check(isVisible())
    }

    @Test
    fun checkRealServiceCommunicationFailure() {
        StandAloneContext.getKoin().loadModules(listOf(
                module { single<PaymentService>(override = true) { PaymentServiceImpl(get()) } }
        ))
        server.enqueue(MockResponse().setResponseCode(406).setBody(""))
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.button_increase)).perform(ViewActions.click())
        onView(withText("Pagar")).perform(ViewActions.click())
        onView(withText("Transação não completada!")).check(isVisible())
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().resources.forEach { IdlingRegistry.getInstance().unregister(it) }
        server.close()
    }

    private fun isVisible() = ViewAssertion { view, _ -> assertThat(view, VisibilityMatcher(View.VISIBLE)) }

}

class TestCoLauncher : CoLauncher {
    override fun launchThis(block: suspend CoroutineScope.() -> Unit): Job {
        return GlobalScope.launch {
            CoroutineIdlingResource.default.run()
            launch { block.invoke(this) }.join()
            CoroutineIdlingResource.default.idle()
        }
    }
}

class CoroutineIdlingResource private constructor() : IdlingResource {

    private val isIdleNow = AtomicBoolean(true)
    private var callback: IdlingResource.ResourceCallback? = null
    fun run() {
        isIdleNow.set(false)
    }

    fun idle() {
        isIdleNow.set(true)
        callback?.onTransitionToIdle()
    }

    override fun getName(): String = this::class.java.name
    override fun isIdleNow() = isIdleNow.get()
    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.callback = callback
    }

    companion object {
        val default = CoroutineIdlingResource()
    }
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