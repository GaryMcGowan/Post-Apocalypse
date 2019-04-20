package com.garymcgowan.postapocalypse.model

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostsModelStore @Inject constructor() : ModelStore<PostState>(PostState.Loading)