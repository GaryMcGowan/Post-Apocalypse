package com.garymcgowan.postapocalypse.view.postlist.mvp

import com.garymcgowan.postapocalypse.core.SchedulerProvider
import com.garymcgowan.postapocalypse.model.Comment
import com.garymcgowan.postapocalypse.model.Post
import com.garymcgowan.postapocalypse.model.User
import com.garymcgowan.postapocalypse.network.PostsApi
import com.garymcgowan.postapocalypse.utils.TestSchedulerProvider
import com.garymcgowan.postapocalypse.view.postlist.PostItemViewState
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import io.reactivex.Single
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.junit.MockitoJUnit

class PostListPresenterTest {

    //RULES
    @get:Rule val mockitoRule = MockitoJUnit.rule()

    //MOCKS
    @Mock lateinit var api: PostsApi
    @Mock lateinit var view: PostListContract.View
    @Spy private val scheduler: SchedulerProvider = TestSchedulerProvider()

    @InjectMocks lateinit var presenter: PostListPresenter

    //HELPER Vals
    private val post = Post(1, 2, "title", "body")
    private val user = User(2, "web", null, "phone", "name")
    private val comment = Comment(2, 1, "name", "body", "email")

    @After
    fun tearDown() {
        verifyNoMoreInteractions(api)
    }

    @Test
    fun `Given user lands on screen, posts are requested`() {

        // Given
        given(api.fetchPosts()).willReturn(Single.just(listOf(post)))
        given(api.fetchUsers()).willReturn(Single.just(listOf(user)))
        given(api.fetchComments()).willReturn(Single.just(listOf(comment)))

        // When
        presenter.takeView(view)

        // Then
        verify(view, times(1)).showPostListLoading()
        verify(view, times(1)).showPostListLoading()
        verify(api, times(1)).fetchPosts()
        verify(api, times(1)).fetchUsers()
        verify(api, times(1)).fetchComments()

        verify(view, times(1)).displayListViewState(
            listOf(
                PostItemViewState(
                    post,
                    user,
                    listOf(comment)
                )
            )
        )

    }


    @Test
    fun `Given user refreshes list, posts are requested`() {

        // Given
        `Given user lands on screen, posts are requested`()

        // When
        presenter.onListRefreshed()

        // Then
        verify(view, times(2)).showPostListLoading()
        verify(view, times(2)).showPostListLoading()
        verify(api, times(2)).fetchPosts()
        verify(api, times(2)).fetchUsers()
        verify(api, times(2)).fetchComments()

        //only once because results haven't changed (distinct only)
        verify(view, times(1)).displayListViewState(
            listOf(
                PostItemViewState(
                    post,
                    user,
                    listOf(comment)
                )
            )
        )
    }


    @Test
    fun `Given user presses a post, the view is told to navigate to details`() {

        // Given
        `Given user lands on screen, posts are requested`()

        // When
        presenter.onItemPressed(post, user)

        // Then
        verify(view, times(1)).showPostListLoading()
        verify(view, times(1)).showPostListLoading()
        verify(api, times(1)).fetchPosts()
        verify(api, times(1)).fetchUsers()
        verify(api, times(1)).fetchComments()
        verify(view, times(1)).displayListViewState(
            listOf(
                PostItemViewState(
                    post,
                    user,
                    listOf(comment)
                )
            )
        )

        verify(view, times(1)).goToPost(post, user)
    }

    @Test
    fun `Given user navigates away, view is disposed of`() {

        // Given
        `Given user lands on screen, posts are requested`()

        // When
        presenter.dropView()

        // Then
        verify(view, times(1)).showPostListLoading()
        verify(view, times(1)).showPostListLoading()
        verify(api, times(1)).fetchPosts()
        verify(api, times(1)).fetchUsers()
        verify(api, times(1)).fetchComments()
        verify(view, times(1)).displayListViewState(
            listOf(
                PostItemViewState(
                    post,
                    user,
                    listOf(comment)
                )
            )
        )
    }


    //region Errors
    @Test
    fun `Given one network error when screen loads, error message is displayed`() {

        // Given
        given(api.fetchPosts()).willReturn(Single.error(Throwable("Something went wrong")))
        given(api.fetchUsers()).willReturn(Single.just(listOf(user)))
        given(api.fetchComments()).willReturn(Single.just(listOf(comment)))

        // When
        presenter.takeView(view)

        // Then
        verify(view, times(1)).showPostListLoading()
        verify(view, times(1)).showPostListLoading()
        verify(api, times(1)).fetchPosts()
        verify(api, times(1)).fetchUsers()
        verify(api, times(1)).fetchComments()
        verify(view, times(1)).displayErrorForPostList()
    }

    @Test
    fun `Given all network error when screen loads, error message is displayed`() {

        // Given
        given(api.fetchPosts()).willReturn(Single.error(Throwable("Something went wrong")))
        given(api.fetchUsers()).willReturn(Single.error(Throwable("Something went wrong")))
        given(api.fetchComments()).willReturn(Single.error(Throwable("Something went wrong")))

        // When
        presenter.takeView(view)

        // Then
        verify(view, times(1)).showPostListLoading()
        verify(view, times(1)).showPostListLoading()
        verify(api, times(1)).fetchPosts()
        verify(api, times(1)).fetchUsers()
        verify(api, times(1)).fetchComments()
        verify(view, times(1)).displayErrorForPostList()
    }
    //endregion

}