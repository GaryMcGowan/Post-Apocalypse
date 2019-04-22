package com.garymcgowan.postapocalypse.view.postdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.garymcgowan.postapocalypse.R
import com.garymcgowan.postapocalypse.model.Comment
import com.garymcgowan.postapocalypse.model.Post
import com.garymcgowan.postapocalypse.view.base.BaseFragment
import com.garymcgowan.postapocalypse.view.postdetails.mvp.PostDetailsContract
import kotlinx.android.synthetic.main.fragment_post_details.*
import javax.inject.Inject


class PostDetailsFragment : BaseFragment(), PostDetailsContract.View {

    @Inject lateinit var presenter: PostDetailsContract.Presenter

    private val args: PostDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_post_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUIForPost(args.post)
    }

    private fun updateUIForPost(post: Post) {
        textView.text = "${post.title}/n/n${post.body}"
    }

    override fun onStart() {
        super.onStart()
        presenter.takeView(this)
        presenter.takePost(args.post)
    }

    override fun onStop() {
        super.onStop()
        presenter.dropView()
    }


    override fun showCommentsLoading() {
    }

    override fun hideCommentsLoading() {
    }

    override fun displayComments(comments: List<Comment>) {
    }

    override fun displayErrorForComments() {
    }
}