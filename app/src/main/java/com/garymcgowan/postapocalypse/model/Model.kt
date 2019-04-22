package com.garymcgowan.postapocalypse.model

import com.garymcgowan.postapocalypse.mvi.intent.Intent
import io.reactivex.Observable


interface Model<S> {
    fun process(intent: Intent<S>)
    fun modelState(): Observable<S>
}