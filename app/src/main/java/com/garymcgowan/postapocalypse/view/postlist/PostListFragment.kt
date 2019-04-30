package com.garymcgowan.postapocalypse.view.postlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.garymcgowan.postapocalypse.R
import com.garymcgowan.postapocalypse.model.Post
import com.garymcgowan.postapocalypse.model.User
import com.garymcgowan.postapocalypse.network.ImageLoader
import com.garymcgowan.postapocalypse.view.base.BaseFragment
import com.garymcgowan.postapocalypse.view.postlist.mvp.PostListContract
import com.google.android.material.snackbar.Snackbar
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
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

    private val groupAdapter = GroupAdapter<ViewHolder>().apply { setHasStableIds(true) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(listRecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = groupAdapter
        }

        bindPresenter()
    }

    private fun bindPresenter() {
        groupAdapter.setOnItemClickListener { item, _ ->
            when (item) {
                is PostItem -> presenter.onItemPressed(
                    item.postItemViewState.post,
                    item.postItemViewState.user
                )
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

    override fun displayListViewState(state: List<PostItemViewState>) {
        if (state.isEmpty()) {
            emptyViewSwitcher.displayedChild = emptyViewSwitcher.indexOfChild(emptyText)
        } else {
            emptyViewSwitcher.displayedChild = emptyViewSwitcher.indexOfChild(listRecyclerView)
            groupAdapter.update(state.map { PostItem(it, imageLoader) })
        }
    }


    override fun displayErrorForPostList() {
        Snackbar.make(coordinatorLayout, getString(R.string.error_post_list), Snackbar.LENGTH_LONG)
            .show()
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