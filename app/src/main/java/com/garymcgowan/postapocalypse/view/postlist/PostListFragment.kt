package com.garymcgowan.postapocalypse.view.postlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.garymcgowan.postapocalypse.R
import com.garymcgowan.postapocalypse.model.Comment
import com.garymcgowan.postapocalypse.model.Post
import com.garymcgowan.postapocalypse.model.User
import com.garymcgowan.postapocalypse.network.ImageLoader
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
    @Inject lateinit var imageLoader: ImageLoader

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_post_list, container, false)
    }

    private val groupAdapter = GroupAdapter<ViewHolder>().apply { hasStableIds() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(listRecyclerView) {
            layoutManager = LinearLayoutManager(context)
//            addItemDecoration(
//                DividerItemDecoration(
//                    context,
//                    DividerItemDecoration.VERTICAL
//                )
//            )
            adapter = groupAdapter
        }

        bindPresenter()
    }

    private fun bindPresenter() {
        groupAdapter.setOnItemClickListener { item, _ ->
            when (item) {
                is PostItem -> presenter.onItemPressed(item.post, item.user)
                else -> throw NotImplementedError("Groupie item click not implemented")
            }
        }

        swipeRefresh.setOnRefreshListener { presenter.onListRefreshed() }
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

    override fun displayPostList(posts: List<Triple<Post, User, List<Comment>>>) {
        groupAdapter.update(posts.map { PostItem(it.first, it.second, it.third, imageLoader) })
    }


    override fun displayErrorForPostList() {
        Toast.makeText(context, "Posts couldn't not be loaded", Toast.LENGTH_LONG).show()
    }

    override fun goToPost(post: Post, user: User) {
        view?.findNavController()
            ?.navigate(
                PostListFragmentDirections.actionNavigationListToNavigationDetails(
                    post,
                    user
                )
            )
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