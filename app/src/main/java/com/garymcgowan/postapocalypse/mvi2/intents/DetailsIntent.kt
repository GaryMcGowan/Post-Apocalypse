package com.garymcgowan.postapocalypse.mvi2.intents

import com.garymcgowan.postapocalypse.model.Comment
import com.garymcgowan.postapocalypse.mvi2.base.MviIntent

sealed class DetailsIntent : MviIntent {
    object LoadCommentsIntent : DetailsIntent()
    object RefreshCommentsIntent : DetailsIntent()
    data class CommentPressedIntent(val comment: Comment) : DetailsIntent()
}