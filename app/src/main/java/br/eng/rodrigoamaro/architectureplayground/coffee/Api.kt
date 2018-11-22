package br.eng.rodrigoamaro.architectureplayground.coffee

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface Api {
    @GET("/coffee/pay")
    fun payForCoffee(): Deferred<Response<Receipt>>
}

class Receipt(val orderNumber: String)