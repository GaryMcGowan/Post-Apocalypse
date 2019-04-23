package com.garymcgowan.postapocalypse.view.postdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.garymcgowan.postapocalypse.R
import com.garymcgowan.postapocalypse.model.Comment
import com.garymcgowan.postapocalypse.model.Post
import com.garymcgowan.postapocalypse.model.User
import com.garymcgowan.postapocalypse.network.ImageLoader
import com.garymcgowan.postapocalypse.view.base.BaseFragment
import com.garymcgowan.postapocalypse.view.postdetails.mvp.PostDetailsContract
import com.google.android.material.snackbar.Snackbar
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_post_details.*
import javax.inject.Inject


class PostDetailsFragment : BaseFragment(), PostDetailsContract.View {

    @Inject lateinit var presenter: PostDetailsContract.Presenter
    @Inject lateinit var imageLoader: ImageLoader

    private val args: PostDetailsFragmentArgs by navArgs()

    private val postSection: Section = Section()
    private val commentSection: Section = Section()

    private val groupAdapter = GroupAdapter<ViewHolder>().apply {
        addAll(listOf(postSection, commentSection))
        setHasStableIds(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_post_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(detailsRecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = groupAdapter
        }
        bindPresenter()
    }

    private fun bindPresenter() {
        swipeRefresh.setOnRefreshListener { presenter.onRefreshComments() }
    }

    override fun onStart() {
        super.onStart()
        presenter.takeView(this)
        presenter.takePostAndUser(args.post, args.user)
    }

    override fun onStop() {
        super.onStop()
        presenter.dropView()
    }

    override fun showCommentsLoading() {
        swipeRefresh.isRefreshing = true
    }

    override fun hideCommentsLoading() {
        swipeRefresh.isRefreshing = false
    }

    override fun displayPostDetails(post: Post, user: User) {
        postSection.add(PostBodyItem(post, user, imageLoader))
    }

    override fun displayComments(comments: List<Comment>) {
        commentSection.update(comments.map { CommentItem(it, imageLoader) })
    }

    override fun displayErrorForComments() {
        Snackbar.make(
            coordinatorLayout,
            getString(R.string.error_post_details),
            Snackbar.LENGTH_LONG
        ).show()
    }
}