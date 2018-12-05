package br.eng.rodrigoamaro.architectureplayground.coffee

import br.eng.rodrigoamaro.architectureplayground.coffee.proto.Receipt

interface PaymentService {
    suspend fun pay(invoice: SaleState): Receipt?
}

class PaymentServiceImpl(private val api: Api) : PaymentService {
    override suspend fun pay(invoice: SaleState): Receipt? {
        val result = api.payForCoffee().await()
        if (!result.isSuccessful) throw IllegalStateException() else return result.body()
    }
}