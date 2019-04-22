package com.garymcgowan.postapocalypse.mvi

import com.garymcgowan.postapocalypse.mvi.state.PostState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostsModelStore @Inject constructor() : ModelStore<PostState>(
    PostState.Loading
)