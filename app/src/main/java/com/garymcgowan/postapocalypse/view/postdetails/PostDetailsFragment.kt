package com.garymcgowan.postapocalypse.view.postdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.garymcgowan.postapocalypse.R
import com.garymcgowan.postapocalypse.model.Post
import com.garymcgowan.postapocalypse.mvi.PostsModelStore
import com.garymcgowan.postapocalypse.mvi.intent.PostsListIntentFactory
import com.garymcgowan.postapocalypse.mvi.state.PostState
import com.garymcgowan.postapocalypse.mvi.state.StateSubscriber
import com.garymcgowan.postapocalypse.mvi.viewevent.EventObservable
import com.garymcgowan.postapocalypse.mvi.viewevent.PostsListViewEvent
import com.garymcgowan.postapocalypse.view.base.BaseFragment
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_post_details.*
import javax.inject.Inject

class PostDetailsFragment : BaseFragment(), EventObservable<PostsListViewEvent>,
    StateSubscriber<PostState> {

    @Inject lateinit var postsModelStore: PostsModelStore
    @Inject lateinit var intentFactory: PostsListIntentFactory

    private val disposables = CompositeDisposable()

    override fun events(): Observable<PostsListViewEvent> = Observable.empty()

    override fun Observable<PostState>.subscribeToState(): Disposable = subscribe { state ->
        when (state) {
            is PostState.PostPressed -> updateUIForPost(state.post)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_post_details, container, false)
    }

    private fun updateUIForPost(post: Post) {
        textView.text = "${post.title}/n/n${post.body}"
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