package br.eng.rodrigoamaro.architectureplayground

import kotlinx.coroutines.delay


interface PaymentService {
    suspend fun pay(invoice: SaleState)
}

class PaymentServiceImpl : PaymentService {
    override suspend fun pay(invoice: SaleState) {
        delay(3000)
    }
}