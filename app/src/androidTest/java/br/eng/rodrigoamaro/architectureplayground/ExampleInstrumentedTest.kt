package br.eng.rodrigoamaro.architectureplayground

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import br.eng.rodrigoamaro.architectureplayground.coffee.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private val environment =
        EnvironmentSetup(InstrumentationRegistry.getInstrumentation().targetContext)

    private val robot = CoffeeBuyerRobot()

    @get:Rule
    val mRule = ActivityTestRule<MainActivity>(MainActivity::class.java, true, false)

    @Before
    fun setUp() {
        environment.setCoffeePrice(250)
        environment.setMockedService()
        environment.setSuccessServiceResponse()
        IdlingRegistry.getInstance().register(CoRoutineIdlingResource.default)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().resources.forEach {
            IdlingRegistry.getInstance().unregister(it)
        }
    }

    @Test
    fun checkCoffeeCount() {
        ActivityScenario.launch(MainActivity::class.java)
        robot.addCoffee()
            .addCoffee()
            .check()
            .howManyCoffees(2)
    }

    @Test
    fun tryToBuySomeCoffee() {
        ActivityScenario.launch(MainActivity::class.java)
        robot.addCoffee()
            .finishOrder()
            .selectPaymentMethod(CoffeeBuyerRobot.PaymentMethod.Debit)
            .check()
            .transactionSucceeded()
    }

    @Test
    fun failToBuyCoffee() {
        environment.setErrorServiceResponse()
        ActivityScenario.launch(MainActivity::class.java)
        robot.addCoffee()
            .finishOrder()
            .selectPaymentMethod(CoffeeBuyerRobot.PaymentMethod.Voucher)
            .check()
            .transactionNotSucceeded()
    }

    @Test
    fun newCoffeePrice() {
        environment.setCoffeePrice(150)
        ActivityScenario.launch(MainActivity::class.java)
        robot.addCoffee()
            .addCoffee()
            .check()
            .priceIs("3.00")
    }

    @Test
    fun validatingTheBackStack() {
        ActivityScenario.launch(MainActivity::class.java)
        robot.addCoffee()
            .finishOrder()
            .selectPaymentMethod(CoffeeBuyerRobot.PaymentMethod.Debit)
            .goBack()
            .goBack()
            .check()
            .isAbleToEditSale()
    }
}
