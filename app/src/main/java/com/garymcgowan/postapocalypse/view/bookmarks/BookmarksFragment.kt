package com.garymcgowan.postapocalypse.view.bookmarks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.garymcgowan.postapocalypse.R
import com.garymcgowan.postapocalypse.view.base.BaseFragment

class BookmarksFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_post_details, container, false)
    }
}