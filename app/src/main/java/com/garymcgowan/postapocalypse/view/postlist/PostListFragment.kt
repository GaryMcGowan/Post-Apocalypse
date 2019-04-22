package com.garymcgowan.postapocalypse.view.postlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.garymcgowan.postapocalypse.R
import com.garymcgowan.postapocalypse.mvi2.base.MviView
import com.garymcgowan.postapocalypse.mvi2.intents.PostListIntent
import com.garymcgowan.postapocalypse.mvi2.viewmodel.PostListViewModel
import com.garymcgowan.postapocalypse.mvi2.viewstate.PostListViewState
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
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_post_list.*
import javax.inject.Inject


open class PostListFragment : BaseFragment(),
    MviView<PostListIntent, PostListViewState> {

    @Inject lateinit var viewModel: PostListViewModel
    private val disposables = CompositeDisposable()


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


    override fun intents(): Observable<PostListIntent> {
        return Observable.merge(
            Observable.just(PostListIntent.LoadAllPostsIntent),
            swipeRefresh.refreshes().map { PostListIntent.RefreshAllPostsIntent },
            groupAdapter.itemClicks().map {
                when (it) {
                    is PostItem -> PostListIntent.PostPressedIntent(it.getPost())
                    else -> throw NotImplementedError("Groupie item click not implemented")
                }
            })
    }

    override fun render(state: PostListViewState) {

        swipeRefresh.isRefreshing = state.isLoading

        groupAdapter.addAll(state.posts.map { PostItem(it) })

        if (state.error != null) {
            Toast.makeText(context, "error", Toast.LENGTH_LONG).show()
        }

    }

    private fun bind() {
        disposables += viewModel.states().subscribe(this::render)
        viewModel.processIntents(intents())
    }

    override fun onStart() {
        super.onStart()
        bind()
    }

    override fun onStop() {
        super.onStop()
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