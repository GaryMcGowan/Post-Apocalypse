package com.garymcgowan.postapocalypse.mvi.intent

interface Intent<T> {
    fun reduce(oldState: T): T
}

fun <T> intent(block: (T) -> T): Intent<T> = object : Intent<T> {
    override fun reduce(oldState: T): T = block(oldState)
}

fun <T> viewEffect(block: (T) -> Unit): Intent<T> = object : Intent<T> {
    override fun reduce(oldState: T): T = oldState.apply(block)
}
