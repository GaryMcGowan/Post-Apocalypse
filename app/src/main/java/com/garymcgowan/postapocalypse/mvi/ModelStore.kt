package com.garymcgowan.postapocalypse.mvi

import com.garymcgowan.postapocalypse.model.Model
import com.garymcgowan.postapocalypse.mvi.intent.Intent
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber


open class ModelStore<S>(initialState: S) : Model<S> {

    private val intentions = PublishRelay.create<Intent<S>>()

    private val store = intentions.observeOn(AndroidSchedulers.mainThread()).scan(initialState) { oldState, intention -> intention.reduce(oldState) }.replay(1).apply { connect() }

    private val internalDisposable = store.subscribe(::internalLogger, ::crashHandler)
    private fun internalLogger(state: S) = Timber.i("$state")
    private fun crashHandler(throwable: Throwable): Unit = throw throwable

    override fun process(intent: Intent<S>) = intentions.accept(intent)

    override fun modelState(): Observable<S> = store

}