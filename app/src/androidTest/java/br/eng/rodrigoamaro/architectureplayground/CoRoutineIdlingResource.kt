package br.eng.rodrigoamaro.architectureplayground

import androidx.test.espresso.IdlingResource
import java.util.concurrent.atomic.AtomicBoolean

class CoRoutineIdlingResource private constructor() : IdlingResource {

    private val isIdleNow = AtomicBoolean(true)
    private var callback: IdlingResource.ResourceCallback? = null
    fun run() {
        println("Running")
        isIdleNow.set(false)
    }

    fun idle() {
        println("Idle")
        isIdleNow.set(true)
        callback?.onTransitionToIdle()
    }

    override fun getName(): String = this::class.java.name
    override fun isIdleNow() = isIdleNow.get()
    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.callback = callback
    }

    companion object {
        val default = CoRoutineIdlingResource()
    }
}