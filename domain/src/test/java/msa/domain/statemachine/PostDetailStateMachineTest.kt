package msa.domain.statemachine

import io.reactivex.Scheduler
import msa.domain.DummyData
import msa.domain.actionstate.PostAction
import msa.domain.actionstate.PostDetailState
import msa.domain.repository.Repository
import msa.domain.usecases.GetPostDetail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

/**
 * Created by Abhi Muktheeswarar.
 */
@RunWith(JUnit4::class)
class PostDetailStateMachineTest {

    private lateinit var postDetailStateMachine: PostDetailStateMachine

    @Before
    @Throws
    fun setUp() {

        val repository = mock(Repository::class.java)

        val threadExecutor = mock(Scheduler::class.java)
        val postExecutionScheduler = mock(Scheduler::class.java)

        val getPostDetail = GetPostDetail(repository, threadExecutor, postExecutionScheduler)

        postDetailStateMachine = PostDetailStateMachine(getPostDetail)

    }

    @Test
    fun testReducer() {

        var state = PostDetailState()

        state = postDetailStateMachine.reducer(state, PostAction.LoadPostDetailAction(1, 1))

        assertNotNull(state.postId)
        assertNotNull(state.userId)

        assert(state.loading)

        state = postDetailStateMachine.reducer(
            state,
            PostAction.PostDetailLoadedAction(
                post = DummyData.getPosts().first(),
                comments = emptyList(),
                user = DummyData.getUsers().first()
            )
        )

        assertNotNull(state.post)
        assertNotNull(state.comments)

        state = postDetailStateMachine.reducer(state, PostAction.RefreshPostsAction)

        assert(state.refreshing)

        state = postDetailStateMachine.reducer(
            state,
            PostAction.PostDetailLoadedAction(
                post = DummyData.getPosts().first(),
                comments = emptyList(),
                user = DummyData.getUsers().first()
            )
        )

        assertFalse(state.loading)
        assertFalse(state.refreshing)
        assertNotNull(state.post)
        assertNotNull(state.comments)

        state = postDetailStateMachine.reducer(
            state,
            PostAction.ErrorLoadingPostsAction(Exception("Error loading post detail"))
        )

        assertFalse(state.loading)
        assertFalse(state.refreshing)
        assertNotNull(state.exception)

    }
}