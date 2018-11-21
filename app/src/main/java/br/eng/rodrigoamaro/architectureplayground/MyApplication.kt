package br.eng.rodrigoamaro.architectureplayground

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.closeKoin
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MyApplication : Application() {
    private val module = module(override = true) {
        single<Api> {
            Retrofit.Builder()
                    .baseUrl("http://localhost:8080/")
                    .addConverterFactory(MoshiConverterFactory.create())
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
        closeKoin()
        startKoin(this, listOf(module))
    }
}