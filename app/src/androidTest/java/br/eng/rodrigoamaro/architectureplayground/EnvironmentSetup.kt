package br.eng.rodrigoamaro.architectureplayground

import android.content.Context
import android.preference.PreferenceManager
import br.eng.rodrigoamaro.architectureplayground.coffee.Api
import br.eng.rodrigoamaro.architectureplayground.coffee.Receipt
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext
import retrofit2.Response
import java.io.IOException

class EnvironmentSetup(private val context: Context) {

    private val mockedService: Api = mockk()

    fun setCoffeePrice(price: Int) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        preferences.edit().putInt("COFFEE_PRICE", price).apply()
    }

    fun setSuccessServiceResponse() {
        coEvery { mockedService.payForCoffee() } returns CompletableDeferred(
            Response.success(
                Receipt("123")
            )
        )
    }

    fun setErrorServiceResponse() {
        coEvery { mockedService.payForCoffee() } throws IOException()
    }

    fun setMockedService() {
        StandAloneContext.getKoin().loadModules(listOf(
            module {
                single<CoLauncher>(override = true) { TestCoLauncher() }
                single(override = true) { mockedService }
            }
        ))
    }

    class TestCoLauncher : CoLauncher {
        override fun launchThis(block: suspend CoroutineScope.() -> Unit): Job {
            return GlobalScope.launch {
                CoRoutineIdlingResource.default.run()
                println("Launched")
                launch { block.invoke(this) }.join()
                println("Operation Finished")
                CoRoutineIdlingResource.default.idle()
            }
        }
    }
}