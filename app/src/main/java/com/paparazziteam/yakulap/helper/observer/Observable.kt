package com.paparazziteam.yakulap.helper.observer

class Observable<T>(var value:T) {

    private val observers:MutableList<(T)->Unit> = mutableListOf()

    fun suscribe(observer:(T) -> Unit){
        observers.add(observer)
        observer(value)
    }

    fun unsuscribe(observer:(T) -> Unit){
        observers.remove(observer)

    }

    fun updateValue(newValue:T){
        value = newValue
        notifyObservers()
    }

    private fun notifyObservers() {
        observers.forEach { it(value) }
    }
}