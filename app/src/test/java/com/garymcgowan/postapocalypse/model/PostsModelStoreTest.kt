package com.garymcgowan.postapocalypse.model

import com.garymcgowan.postapocalypse.mvi.PostsModelStore
import com.garymcgowan.postapocalypse.mvi.intent.intent
import com.garymcgowan.postapocalypse.mvi.state.PostState
import com.garymcgowan.postapocalypse.utils.ReplaceMainThreadSchedulerRule
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.junit.MockitoJUnit

class PostsModelStoreTest {

    //RULES
    @get:Rule val mockitoRule = MockitoJUnit.rule()
    @get:Rule val schedulerRule = ReplaceMainThreadSchedulerRule()

    //MOCKS
    @InjectMocks lateinit var postsModelStore: PostsModelStore

    //TESTS
    @Test
    fun `Starting state is Loading`() {
        val testObs = postsModelStore.modelState().test()
        testObs.assertValue(PostState.Loading)
    }

    @Test
    fun `State Changes`() {

        postsModelStore.process(intent {
            PostState.Error(
                Throwable("test")
            )
        })

        val testObs = postsModelStore.modelState().test()

        testObs.assertValueCount(1)
        assert(testObs.values().last() is PostState.Error)

    }

}