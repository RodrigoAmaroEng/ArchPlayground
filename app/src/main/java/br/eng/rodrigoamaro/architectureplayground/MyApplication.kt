package br.eng.rodrigoamaro.architectureplayground

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.closeKoin

class MyApplication : Application() {
    private val module = module(override = true) {
        single<PaymentService> { PaymentServiceImpl() }
        single<SharedPreferences> { PreferenceManager.getDefaultSharedPreferences(this@MyApplication) }
        single<Settings> { SettingsImpl(get()) }
    }

    override fun onCreate() {
        super.onCreate()
        closeKoin()
        startKoin(this, listOf(module))
    }
}