package com.garymcgowan.postapocalypse.mvi2.processing

import com.garymcgowan.postapocalypse.core.SchedulerProvider
import com.garymcgowan.postapocalypse.mvi2.actions.PostListAction
import com.garymcgowan.postapocalypse.mvi2.actions.PostListAction.LoadAllPostsAction
import com.garymcgowan.postapocalypse.mvi2.results.PostListResult
import com.garymcgowan.postapocalypse.mvi2.results.PostListResult.LoadAllPostsResult
import com.garymcgowan.postapocalypse.network.PostsApi
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import timber.log.Timber
import javax.inject.Inject

class PostListProcessorHolder @Inject constructor(
    private val api: PostsApi,
    private val schedulers: SchedulerProvider
) {

    private val loadAllPostsProcessor =
        ObservableTransformer<LoadAllPostsAction, LoadAllPostsResult> { actions ->
            actions.flatMap {
                api.fetchPosts().toObservable()
                    .map { LoadAllPostsResult.Success(it) }
                    .cast(LoadAllPostsResult::class.java)
                    .onErrorReturn(LoadAllPostsResult::Failure)
                    .subscribeOn(schedulers.io())
                    .observeOn(schedulers.ui())
                    .startWith(LoadAllPostsResult.Loading)
            }
        }

    private val postPressedProcessor =
        ObservableTransformer<PostListAction.PostPressedAction, PostListResult.PostPressedResult> { actions ->
            actions.map { PostListResult.PostPressedResult.Success(it.post) }
//            actions.flatMap {
//                api.fetchPosts().toObservable()
//                    .map { LoadAllPostsResult.Success(it) }
//                    .cast(LoadAllPostsResult::class.java)
//                    .onErrorReturn (LoadAllPostsResult::Failure)
//                    .subscribeOn(schedulers.io())
//                    .observeOn(schedulers.ui())
//                    .startWith(LoadAllPostsResult.Loading)
//            }
        }

    internal var actionProcessor =
        ObservableTransformer<PostListAction, PostListResult> { actions ->
            actions.publish { shared ->
                Observable.merge(
                    shared.ofType(LoadAllPostsAction::class.java).compose(loadAllPostsProcessor),
                    shared.ofType(PostListAction.PostPressedAction::class.java).compose(
                        postPressedProcessor
                    )
//                    .map { it as PostListResult }
                )
                    .mergeWith(
                        shared.filter { v ->
                            v !is LoadAllPostsAction
                                    && v !is PostListAction.PostPressedAction
                        }.flatMap { w ->
                            Timber.d("actionProcessor not implemented for ${w.javaClass}")
                            Observable.error<PostListResult>(IllegalArgumentException("Action Not Defined $w"))
                        }
                    )
            }
        }
}