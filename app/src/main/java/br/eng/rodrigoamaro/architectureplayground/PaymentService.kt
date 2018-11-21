package br.eng.rodrigoamaro.architectureplayground


interface PaymentService {
    suspend fun pay(invoice: SaleState)
}

class PaymentServiceImpl(private val api: Api) : PaymentService {
    override suspend fun pay(invoice: SaleState) {
        val result = api.payForCoffee().await()
        if (!result.isSuccessful) {
            throw IllegalStateException()
        }
    }
}