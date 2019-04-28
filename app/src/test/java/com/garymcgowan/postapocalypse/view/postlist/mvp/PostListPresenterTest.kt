package com.garymcgowan.postapocalypse.view.postlist.mvp

import com.garymcgowan.postapocalypse.core.SchedulerProvider
import com.garymcgowan.postapocalypse.model.Comment
import com.garymcgowan.postapocalypse.model.Post
import com.garymcgowan.postapocalypse.model.User
import com.garymcgowan.postapocalypse.network.NetworkRepository
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
    @Mock lateinit var network: NetworkRepository
    @Mock lateinit var view: PostListContract.View
    @Spy private val scheduler: SchedulerProvider = TestSchedulerProvider()

    @InjectMocks lateinit var presenter: PostListPresenter

    //HELPER Vals
    private val post = Post(1, 2, "title", "body")
    private val user = User(2, "web", null, "phone", "name")
    private val comment = Comment(2, 1, "name", "body", "email")

    @After
    fun tearDown() {
        verifyNoMoreInteractions(network)
    }

    @Test
    fun `Given user lands on screen, posts are requested`() {

        // Given
        given(network.getPosts()).willReturn(Single.just(listOf(post)))
        given(network.getUsers()).willReturn(Single.just(listOf(user)))
        given(network.getComments()).willReturn(Single.just(listOf(comment)))

        // When
        presenter.takeView(view)

        // Then
        verify(view, times(1)).showPostListLoading()
        verify(view, times(1)).showPostListLoading()
        verify(network, times(1)).getPosts()
        verify(network, times(1)).getUsers()
        verify(network, times(1)).getComments()

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
        verify(network, times(2)).getPosts()
        verify(network, times(2)).getUsers()
        verify(network, times(2)).getComments()

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
        verify(network, times(1)).getPosts()
        verify(network, times(1)).getUsers()
        verify(network, times(1)).getComments()
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
        verify(network, times(1)).getPosts()
        verify(network, times(1)).getUsers()
        verify(network, times(1)).getComments()
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
        given(network.getPosts()).willReturn(Single.error(Throwable("Something went wrong")))
        given(network.getUsers()).willReturn(Single.just(listOf(user)))
        given(network.getComments()).willReturn(Single.just(listOf(comment)))

        // When
        presenter.takeView(view)

        // Then
        verify(view, times(1)).showPostListLoading()
        verify(view, times(1)).showPostListLoading()
        verify(network, times(1)).getPosts()
        verify(network, times(1)).getUsers()
        verify(network, times(1)).getComments()
        verify(view, times(1)).displayErrorForPostList()
    }

    @Test
    fun `Given all network error when screen loads, error message is displayed`() {

        // Given
        given(network.getPosts()).willReturn(Single.error(Throwable("Something went wrong")))
        given(network.getUsers()).willReturn(Single.error(Throwable("Something went wrong")))
        given(network.getComments()).willReturn(Single.error(Throwable("Something went wrong")))

        // When
        presenter.takeView(view)

        // Then
        verify(view, times(1)).showPostListLoading()
        verify(view, times(1)).showPostListLoading()
        verify(network, times(1)).getPosts()
        verify(network, times(1)).getUsers()
        verify(network, times(1)).getComments()
        verify(view, times(1)).displayErrorForPostList()
    }
    //endregion

}