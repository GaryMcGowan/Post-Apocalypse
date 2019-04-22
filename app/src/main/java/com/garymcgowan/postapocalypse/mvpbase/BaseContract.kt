package com.garymcgowan.postapocalypse.mvpbase

import io.reactivex.disposables.CompositeDisposable


interface BaseContract {
    interface View {
    }

//    interface Presenter<V> {
//        var view: V?
//
//        fun takeView(view: V) {
//            this.view = view
//            publish()
//        }
//
//        fun dropView() {
//            this.view = null
//        }
//
//        fun publish() {}
//        fun trackScreen() {}
//    }

    abstract class BasePresenter<V> {
        internal var view: V? = null

        internal val disposables = CompositeDisposable()

        open fun takeView(view: V) {
            this.view = view
            publish()
        }

        internal fun dropView() {
            disposables.clear()
        }

        internal fun publish() {}
    }
}