package com.garymcgowan.postapocalypse.mvi.viewevent

import io.reactivex.Observable

interface EventObservable<E> {
    fun events(): Observable<E>
}