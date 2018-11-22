package br.eng.rodrigoamaro.architectureplayground

import android.preference.PreferenceManager
import androidx.test.InstrumentationRegistry
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import br.eng.rodrigoamaro.architectureplayground.coffee.Api
import br.eng.rodrigoamaro.architectureplayground.coffee.MainActivity
import br.eng.rodrigoamaro.architectureplayground.coffee.Receipt
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private val mockedService: Api = mockk()

    @get:Rule
    val mRule = ActivityTestRule<MainActivity>(MainActivity::class.java, true, false)

    @Before
    fun setUp() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(InstrumentationRegistry.getTargetContext())
        preferences.edit().putInt("COFFEE_PRICE", 250).apply()
        coEvery { mockedService.payForCoffee() } returns CompletableDeferred(Response.success(Receipt("123")))
        StandAloneContext.getKoin().loadModules(listOf(
                module {
                    single<CoLauncher>(override = true) { TestCoLauncher() }
                    single(override = true) { mockedService }
                }
        ))
        IdlingRegistry.getInstance().register(CoroutineIdlingResource.default)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().resources.forEach { IdlingRegistry.getInstance().unregister(it) }
    }

    @Test
    fun tryToBuySomeCoffee() {
        ActivityScenario.launch(MainActivity::class.java)
        Espresso.onView(withId(R.id.button_increase)).perform(click())
        Espresso.onView(withText("1")).check(matches(isDisplayed()))
        Espresso.onView(withText("Continuar")).perform(click())
        Espresso.onView(withText("Débito")).perform(click())
        Espresso.onView(withText("Transação concluída!")).check(matches(isDisplayed()))
    }

    @Test
    fun failToBuyCoffee() {
        coEvery { mockedService.payForCoffee() } throws IOException()
        ActivityScenario.launch(MainActivity::class.java)
        Espresso.onView(withId(R.id.button_increase)).perform(click())
        Espresso.onView(withText("Continuar")).perform(click())
        Espresso.onView(withText("Voucher")).perform(click())
        Espresso.onView(withText("Transação não completada!")).check(matches(isDisplayed()))
    }

    @Test
    fun newCoffeePrice() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(InstrumentationRegistry.getTargetContext())
        preferences.edit().putInt("COFFEE_PRICE", 150).apply()
        ActivityScenario.launch(MainActivity::class.java)
        Espresso.onView(withId(R.id.button_increase)).perform(click())
        Espresso.onView(withId(R.id.button_increase)).perform(click())
        Espresso.onView(withText("R$ 3.00")).check(matches(isDisplayed()))
    }

    @Test
    fun validatingTheBackStack() {
        ActivityScenario.launch(MainActivity::class.java)
        Espresso.onView(withId(R.id.button_increase)).perform(click())
        Espresso.onView(withId(R.id.button_increase)).perform(click())
        Espresso.onView(withText("R$ 5.00")).check(matches(isDisplayed()))
        Espresso.onView(withText("Continuar")).perform(click())
        Espresso.onView(withText("Débito")).perform(click())
        Espresso.pressBack()
        Espresso.pressBack()
        Espresso.onView(withId(R.id.button_increase)).check(matches(isEnabled()))
    }

    class TestCoLauncher : CoLauncher {
        override fun launchThis(block: suspend CoroutineScope.() -> Unit): Job {
            return GlobalScope.launch {
                CoroutineIdlingResource.default.run()
                println("Launched")
                launch { block.invoke(this) }.join()
                println("Operation Finished")
                CoroutineIdlingResource.default.idle()
            }
        }
    }

    class CoroutineIdlingResource private constructor() : IdlingResource {

        private val isIdleNow = AtomicBoolean(true)
        private var callback: IdlingResource.ResourceCallback? = null
        fun run() {
            println("Running")
            isIdleNow.set(false)
        }

        fun idle() {
            println("Idle")
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
}
