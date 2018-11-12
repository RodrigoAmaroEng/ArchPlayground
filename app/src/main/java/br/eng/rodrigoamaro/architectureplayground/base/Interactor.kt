package br.eng.rodrigoamaro.architectureplayground.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent


interface Interactor : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun turnOn()

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun turnOff()
}