package br.eng.rodrigoamaro.architectureplayground

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import br.eng.rodrigoamaro.architectureplayground.coffee.Api
import br.eng.rodrigoamaro.architectureplayground.coffee.PaymentService
import br.eng.rodrigoamaro.architectureplayground.coffee.PaymentServiceImpl
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.stopKoin
import retrofit2.Retrofit
import retrofit2.converter.wire.WireConverterFactory

class MyApplication : Application() {
    private val module = module(override = true) {
        single<Api> {
            Retrofit.Builder()
                .baseUrl("http://localhost:8080/")
                .addConverterFactory(WireConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build().create(Api::class.java)
        }
        single<CoLauncher> { CoLauncherImpl() }
        single<PaymentService> { PaymentServiceImpl(get()) }
        single<SharedPreferences> { PreferenceManager.getDefaultSharedPreferences(this@MyApplication) }
        single<Settings> { SettingsImpl(get()) }
    }

    override fun onCreate() {
        super.onCreate()
        stopKoin()
        startKoin(this, listOf(module))
    }
}