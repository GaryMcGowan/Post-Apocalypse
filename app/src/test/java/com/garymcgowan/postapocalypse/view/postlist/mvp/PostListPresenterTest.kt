package com.garymcgowan.postapocalypse.view.postlist.mvp

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

class PostListPresenterTest {


    //RULES
    @get:Rule val mockitoRule = MockitoJUnit.rule()

    @Mock lateinit var api: PostsApi
    @Mock lateinit var view: PostListContract.View
    @Spy private val scheduler: SchedulerProvider = TestSchedulerProvider()


    //MOCKS
    @InjectMocks lateinit var presenter: PostListPresenter

    @After
    fun tearDown() {
        verifyNoMoreInteractions(api)
    }

    private val post = Post(1, 2, "title", "body")
    private val user = User(2, "web", null, "phone", "name")
    private val comment = Comment(2, 1, "name", "body", "email")

    @Test
    fun `take view and refresh`() {

        given(api.fetchPosts()).willReturn(Single.just(listOf(post)))
        given(api.fetchUsers()).willReturn(Single.just(listOf(user)))
        given(api.fetchComments()).willReturn(Single.just(listOf(comment)))

        presenter.takeView(view)

        presenter.onListRefreshed()


        verify(view, times(2)).showPostListLoading()
        verify(view, times(2)).showPostListLoading()
        verify(api, times(2)).fetchPosts()
        verify(api, times(2)).fetchUsers()
        verify(api, times(2)).fetchComments()

        verify(view, times(2)).displayPostList(listOf(Triple(post, user, listOf(comment))))
    }
}