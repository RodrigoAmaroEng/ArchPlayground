package br.eng.rodrigoamaro.architectureplayground

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

interface CoLauncher {
    fun launchThis(block: suspend CoroutineScope.() -> Unit): Job
}

class CoLauncherImpl : CoLauncher {
    override fun launchThis(block: suspend CoroutineScope.() -> Unit): Job {
        return GlobalScope.launch { block.invoke(this) }
    }
}