package com.garymcgowan.postapocalypse

import android.os.Bundle
import com.garymcgowan.postapocalypse.intention.PostsListIntentFactory
import com.garymcgowan.postapocalypse.model.PostState
import com.garymcgowan.postapocalypse.model.PostState.*
import com.garymcgowan.postapocalypse.model.PostsModelStore
import com.garymcgowan.postapocalypse.view.EventObservable
import com.garymcgowan.postapocalypse.view.StateSubscriber
import com.garymcgowan.postapocalypse.view.postslist.PostsListViewEvent
import com.garymcgowan.postapocalypse.view.postslist.PostsListViewEvent.InitPostsList
import com.garymcgowan.postapocalypse.view.postslist.PostsListViewEvent.RefreshPostsList
import com.jakewharton.rxbinding3.swiperefreshlayout.refreshes
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), EventObservable<PostsListViewEvent>, StateSubscriber<PostState> {

    @Inject lateinit var postsModelStore: PostsModelStore
    @Inject lateinit var intentFactory: PostsListIntentFactory

    private val disposables = CompositeDisposable()

    override fun events(): Observable<PostsListViewEvent> = Observable.merge(Observable.just(InitPostsList), swipeRefresh.refreshes().map { RefreshPostsList })

    override fun Observable<PostState>.subscribeToState(): Disposable = subscribe { state ->
        when (state) {
            Loading -> swipeRefresh.isRefreshing = true
            is Content -> {
                textView.text = state.posts.toString()
                swipeRefresh.isRefreshing = false
            }
            is Error -> {
                textView.text = state.throwable.toString()
                swipeRefresh.isRefreshing = false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        disposables += postsModelStore.modelState().subscribeToState()
        disposables += events().subscribe(intentFactory::process)
    }

    override fun onPause() {
        super.onPause()
        disposables.clear()
    }

}
