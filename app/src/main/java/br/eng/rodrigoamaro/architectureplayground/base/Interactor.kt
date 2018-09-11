package br.eng.rodrigoamaro.architectureplayground.base

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent


interface Interactor : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun turnOn()

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun turnOff()
}