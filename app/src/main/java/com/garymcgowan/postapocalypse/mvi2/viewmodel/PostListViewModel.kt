package com.garymcgowan.postapocalypse.mvi2.viewmodel


import com.garymcgowan.postapocalypse.extensions.notOfType
import com.garymcgowan.postapocalypse.mvi2.actions.PostListAction
import com.garymcgowan.postapocalypse.mvi2.base.MviViewModel
import com.garymcgowan.postapocalypse.mvi2.intents.PostListIntent
import com.garymcgowan.postapocalypse.mvi2.processing.PostListProcessorHolder
import com.garymcgowan.postapocalypse.mvi2.results.PostListResult
import com.garymcgowan.postapocalypse.mvi2.results.PostListResult.LoadAllPostsResult
import com.garymcgowan.postapocalypse.mvi2.viewstate.PostListViewState
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

data class PostListViewModel @Inject constructor(
    private val actionProcessorHolder: PostListProcessorHolder
) : MviViewModel<PostListIntent, PostListViewState> {

    private val intentsSubject: PublishSubject<PostListIntent> = PublishSubject.create()
    private val stateObservable: Observable<PostListViewState> = compose()

    private val intentFilter: ObservableTransformer<PostListIntent, PostListIntent>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge(
                    shared.ofType(PostListIntent.LoadAllPostsIntent::class.java).take(1),
                    shared.notOfType(PostListIntent.LoadAllPostsIntent::class.java)
                )
            }
        }

    override fun processIntents(intents: Observable<PostListIntent>) {
        intents.subscribe(intentsSubject)
    }

    private fun compose(): Observable<PostListViewState> {
        return intentsSubject
//            .compose(intentFilter)
            .map(this::actionFromIntent)
            .compose(actionProcessorHolder.actionProcessor)
            .scan(PostListViewState.idle(), reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    override fun states(): Observable<PostListViewState> = stateObservable

    private fun actionFromIntent(intent: PostListIntent): PostListAction {
        return when (intent) {
            is PostListIntent.LoadAllPostsIntent -> PostListAction.LoadAllPostsAction
            is PostListIntent.RefreshAllPostsIntent -> PostListAction.LoadAllPostsAction
            is PostListIntent.PostPressedIntent -> PostListAction.PostPressedAction(intent.post)

        }
    }

    companion object {
        private val reducer =
            BiFunction { previousState: PostListViewState, result: PostListResult ->
                when (result) {
                    is LoadAllPostsResult -> when (result) {
                        is LoadAllPostsResult.Success -> previousState.copy(
                            isLoading = false,
                            posts = result.posts
                        )
                        is LoadAllPostsResult.Failure -> previousState.copy(
                            isLoading = false,
                            error = result.error
                        )
                        is LoadAllPostsResult.Loading -> previousState.copy(isLoading = true)
                    }
                }
            }

    }
}