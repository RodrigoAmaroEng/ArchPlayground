package br.eng.rodrigoamaro.architectureplayground.coffee

import br.eng.rodrigoamaro.architectureplayground.coffee.proto.Receipt
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface Api {
    @GET("/coffee/pay")
    fun payForCoffee(): Deferred<Response<Receipt>>
}