package msa.domain.statemachine

import io.reactivex.Scheduler
import msa.domain.actionstate.PostAction
import msa.domain.actionstate.PostListState
import msa.domain.repository.Repository
import msa.domain.usecases.GetPosts
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
class PostListStateMachineTest {

    private lateinit var postListStateMachine: PostListStateMachine

    @Before
    @Throws
    fun setUp() {

        val repository = mock(Repository::class.java)

        val threadExecutor = mock(Scheduler::class.java)
        val postExecutionScheduler = mock(Scheduler::class.java)

        val getPosts = GetPosts(repository, threadExecutor, postExecutionScheduler)

        postListStateMachine = PostListStateMachine(getPosts)

    }

    @Test
    fun testReducer() {

        var state = PostListState()

        state = postListStateMachine.reducer(state, PostAction.LoadPostsAction)

        assert(state.loading)

        state = postListStateMachine.reducer(state, PostAction.PostsLoadedAction(emptyList()))

        assertNotNull(state.posts)

        state = postListStateMachine.reducer(state, PostAction.RefreshPostsAction)

        assert(state.refreshing)

        state = postListStateMachine.reducer(state, PostAction.PostsLoadedAction(emptyList()))

        assertFalse(state.loading)
        assertFalse(state.refreshing)
        assertNotNull(state.posts)

        state = postListStateMachine.reducer(
            state,
            PostAction.ErrorLoadingPostsAction(Exception("Error loading posts"))
        )

        assertFalse(state.loading)
        assertFalse(state.refreshing)
        assertNotNull(state.exception)

    }
}