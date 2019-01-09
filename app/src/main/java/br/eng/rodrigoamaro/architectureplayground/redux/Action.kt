package br.eng.rodrigoamaro.architectureplayground.redux

open class Action

data class ReduxRestoreStateAction<T>(val state: T?) : Action()