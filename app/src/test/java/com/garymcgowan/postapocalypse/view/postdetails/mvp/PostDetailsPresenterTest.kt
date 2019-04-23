package com.garymcgowan.postapocalypse.view.postdetails.mvp

import com.garymcgowan.postapocalypse.core.SchedulerProvider
import com.garymcgowan.postapocalypse.model.Comment
import com.garymcgowan.postapocalypse.model.Post
import com.garymcgowan.postapocalypse.model.User
import com.garymcgowan.postapocalypse.network.PostsApi
import com.garymcgowan.postapocalypse.utils.TestSchedulerProvider
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

class PostDetailsPresenterTest {

    //RULES
    @get:Rule val mockitoRule = MockitoJUnit.rule()

    //MOCKS
    @Mock lateinit var api: PostsApi
    @Mock lateinit var view: PostDetailsContract.View
    @Spy private val scheduler: SchedulerProvider = TestSchedulerProvider()

    @InjectMocks lateinit var presenter: PostDetailsPresenter

    //HELPER Vals
    private val post = Post(1, 2, "title", "body")
    private val user = User(2, "web", null, "phone", "name")
    private val comment = Comment(2, 1, "name", "body", "email")

    @After
    fun tearDown() {
        verifyNoMoreInteractions(api)
    }

    @Test
    fun `Given user lands on screen, comments are requested`() {

        // Given
        given(api.fetchComments()).willReturn(Single.just(listOf(comment)))

        // When
        presenter.takeView(view)
        presenter.takePostAndUser(post, user)

        // Then
        verify(view, times(1)).showCommentsLoading()
        verify(view, times(1)).showCommentsLoading()
        verify(api, times(1)).fetchComments()

        verify(view, times(1)).displayComments(listOf(comment))

    }

    @Test
    fun `Given user refreshes list, comments are requested`() {

        // Given
        `Given user lands on screen, comments are requested`()

        // When
        presenter.onRefreshComments()

        // Then
        verify(view, times(2)).showCommentsLoading()
        verify(view, times(2)).showCommentsLoading()
        verify(api, times(2)).fetchComments()

        verify(view, times(2)).displayComments(listOf(comment))
    }

    @Test
    fun `Given user navigates away, view is disposed of`() {

        // Given
        `Given user lands on screen, comments are requested`()

        // When
        presenter.dropView()

        // Then
        verify(view, times(1)).showCommentsLoading()
        verify(view, times(1)).showCommentsLoading()
        verify(api, times(1)).fetchComments()

        verify(view, times(1)).displayComments(listOf(comment))
    }


    //region Errors
    @Test
    fun `Given network error when screen loads, error message is displayed`() {

        // Given
        given(api.fetchComments()).willReturn(Single.error(Throwable("Something went wrong")))

        // When
        presenter.takeView(view)
        presenter.takePostAndUser(post, user)

        // Then
        verify(view, times(1)).showCommentsLoading()
        verify(view, times(1)).hideCommentsLoading()
        verify(api, times(1)).fetchComments()
        verify(view, times(1)).displayErrorForComments()
    }
    //endregion
}