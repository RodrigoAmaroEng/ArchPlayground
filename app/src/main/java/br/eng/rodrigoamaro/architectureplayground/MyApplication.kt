package br.eng.rodrigoamaro.architectureplayground

import android.app.Application
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.module

class MyApplication : Application() {
    private val module = module {
        single<PaymentService> { PaymentServiceImpl() }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(module))
    }
}