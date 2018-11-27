package br.eng.rodrigoamaro.architectureplayground

import android.content.Context
import android.preference.PreferenceManager
import androidx.test.espresso.IdlingRegistry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext
import java.net.HttpURLConnection

class EnvironmentSetup(private val context: Context) {

    private val server = MockWebServer()
    private val dispatcher = CustomDispatcher()

    fun setCoffeePrice(price: Int) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        preferences.edit().putInt("COFFEE_PRICE", price).apply()
    }

    fun setSuccessServiceResponse() {
        dispatcher.queue.add(
            MockResponse().setBody("{\"orderNumber\":\"1234\"}").setResponseCode(
                200
            )
        )
    }

    fun setErrorServiceResponse() {
        dispatcher.queue.clear()
        dispatcher.queue.add(MockResponse().setResponseCode(HttpURLConnection.HTTP_CONFLICT))
    }

    fun setMockedService() {
        server.start(8080)
        server.setDispatcher(dispatcher)
        StandAloneContext.getKoin().loadModules(listOf(
            module {
                single<CoLauncher>(override = true) { TestCoLauncher() }
            }
        ))
    }

    fun reset() {
        server.close()
        IdlingRegistry.getInstance().resources.forEach {
            IdlingRegistry.getInstance().unregister(it)
        }
    }

    class TestCoLauncher : CoLauncher {
        override fun launchThis(block: suspend CoroutineScope.() -> Unit): Job {
            return GlobalScope.launch {
                CoRoutineIdlingResource.default.run()
                launch { block.invoke(this) }.join()
                CoRoutineIdlingResource.default.idle()
            }
        }
    }

    class CustomDispatcher : Dispatcher() {
        val queue = mutableListOf<MockResponse>()
        override fun dispatch(request: RecordedRequest?): MockResponse = queue.removeAt(0)
    }
}