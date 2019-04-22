package com.garymcgowan.postapocalypse.view.postlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.garymcgowan.postapocalypse.R
import com.garymcgowan.postapocalypse.mvi.PostsModelStore
import com.garymcgowan.postapocalypse.mvi.intent.PostsListIntentFactory
import com.garymcgowan.postapocalypse.mvi.state.PostState
import com.garymcgowan.postapocalypse.mvi.state.StateSubscriber
import com.garymcgowan.postapocalypse.mvi.viewevent.EventObservable
import com.garymcgowan.postapocalypse.mvi.viewevent.PostsListViewEvent
import com.garymcgowan.postapocalypse.view.base.BaseFragment
import com.jakewharton.rxbinding3.swiperefreshlayout.refreshes
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.ViewHolder
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_post_list.*
import javax.inject.Inject


open class PostListFragment : BaseFragment(), EventObservable<PostsListViewEvent>,
    StateSubscriber<PostState> {

    @Inject lateinit var postsModelStore: PostsModelStore
    @Inject lateinit var intentFactory: PostsListIntentFactory

    private val disposables = CompositeDisposable()

    override fun events(): Observable<PostsListViewEvent> = Observable.merge(Observable.just(
        PostsListViewEvent.InitPostsList
    ),
        swipeRefresh.refreshes().map { PostsListViewEvent.RefreshPostsList },
        groupAdapter.itemClicks().map {
            when (it) {
                is PostItem -> PostsListViewEvent.PostPressed(it.getPost())
                else -> throw NotImplementedError("Groupie item click not implemented")
            }
        })

    override fun Observable<PostState>.subscribeToState(): Disposable = subscribe { state ->
        when (state) {
            PostState.Loading -> swipeRefresh.isRefreshing = true
            is PostState.Content -> {
                groupAdapter.addAll(state.posts.map { PostItem(it) })
                swipeRefresh.isRefreshing = false
            }
            is PostState.Error -> {
                swipeRefresh.isRefreshing = false
            }
            is PostState.PostPressed -> {
                view?.let {
                    Navigation.findNavController(it).navigate(R.id.navigation_details)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_post_list, container, false)
    }

    private val groupAdapter = GroupAdapter<ViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listRecyclerView.layoutManager = LinearLayoutManager(context)
        listRecyclerView.adapter = groupAdapter

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

private fun GroupAdapter<ViewHolder>.itemClicks(): Observable<Item<*>> = ItemClicksObservable(this)
class ItemClicksObservable(private val groupAdapter: GroupAdapter<ViewHolder>) :
    Observable<Item<*>>() {

    override fun subscribeActual(observer: Observer<in Item<*>>) {
        val listener = Listener(groupAdapter, observer)
        observer.onSubscribe(listener)
        groupAdapter.setOnItemClickListener(listener)
    }

    private class Listener(
        private val groupAdapter: GroupAdapter<ViewHolder>,
        private val observer: Observer<in Item<*>>
    ) : MainThreadDisposable(), OnItemClickListener {

        override fun onItemClick(item: Item<*>, view: View) {
            if (!isDisposed) {
                observer.onNext(item)
            }
        }

        override fun onDispose() {
            groupAdapter.setOnItemClickListener(null)
        }
    }
}