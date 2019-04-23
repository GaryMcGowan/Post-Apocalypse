package com.garymcgowan.postapocalypse.mvpbase

import io.reactivex.disposables.CompositeDisposable


interface BaseContract {
    interface View

    abstract class BasePresenter<V> {
        internal var view: V? = null

        internal val disposables = CompositeDisposable()

        open fun takeView(view: V) {
            this.view = view
            publish()
        }

        open fun dropView() {
            disposables.clear()
        }

        open fun publish() {}
    }
}