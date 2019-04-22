package com.garymcgowan.postapocalypse.view.postlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.garymcgowan.postapocalypse.R
import com.garymcgowan.postapocalypse.model.Post
import com.garymcgowan.postapocalypse.view.base.BaseFragment
import com.garymcgowan.postapocalypse.view.postlist.mvp.PostListContract
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.ViewHolder
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable
import kotlinx.android.synthetic.main.fragment_post_list.*
import javax.inject.Inject


open class PostListFragment : BaseFragment(), PostListContract.View {

    @Inject lateinit var presenter: PostListContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_post_list, container, false)
    }

    private val groupAdapter = GroupAdapter<ViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        listRecyclerView.layoutManager = LinearLayoutManager(context)
        listRecyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        listRecyclerView.adapter = groupAdapter

        groupAdapter.setOnItemClickListener { item, _ ->
            when (item) {
                is PostItem -> presenter.onItemPressed(item.getPost())
                else -> throw NotImplementedError("Groupie item click not implemented")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.takeView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.dropView()
    }

    override fun showPostListLoading() {
        swipeRefresh.isRefreshing = true
    }

    override fun hidePostListLoading() {
        swipeRefresh.isRefreshing = false
    }

    override fun displayPostList(posts: List<Post>) {
        groupAdapter.addAll(posts.map { PostItem(it) })
    }

    override fun displayErrorForPostList() {
        Toast.makeText(context, "Posts couldn't not be loaded", Toast.LENGTH_LONG).show()
    }

    override fun goToPost(post: Post) {
//        context?.let {  NavDeepLinkBuilder(it)
//            .setGraph(R.navigation.nav_graph)
//            .setDestination(R.id.navigation_details)
//            .setArguments(Bundle().apply {
//                putParcelable("post", post)
//            })
//            .createPendingIntent().send()
//        }
//        val action = PostDetailsFragmentDirections.
        view?.findNavController()
            ?.navigate(PostListFragmentDirections.actionNavigationListToNavigationDetails(post))
//        view?.let { Navigation.findNavController(it).navigate(
//            PostDetailsFragmentArgs.
//            R.id.navigation_details
//        ) }
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