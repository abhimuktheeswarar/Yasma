package msa.domain.usecases

import com.github.kittinunf.result.Result
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.observers.TestObserver
import msa.domain.DummyData
import msa.domain.actionstate.PostAction
import msa.domain.actionstate.PostListState
import msa.domain.core.Action
import msa.domain.entities.Params
import msa.domain.repository.Repository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock


/**
 * Created by Abhi Muktheeswarar.
 */
@RunWith(JUnit4::class)
class GetPostsTest {

    private lateinit var repository: Repository
    private lateinit var getPosts: GetPosts

    @Before
    @Throws
    fun setUp() {

        val posts = DummyData.getPosts()
        val users = DummyData.getUsers()

        repository = mock(Repository::class.java)

        val threadExecutor = mock(Scheduler::class.java)
        val postExecutionScheduler = mock(Scheduler::class.java)

        getPosts = GetPosts(repository, threadExecutor, postExecutionScheduler)


        `when`(repository.getPosts(Params(true))).thenReturn(Observable.just(Result.of(posts)))
        `when`(repository.getUsers(Params(true))).thenReturn(Observable.just(Result.of(users)))

    }

    @Test
    fun testGetPosts() {

        val testObserver = TestObserver<Action>()

        getPosts.buildUseCaseObservable(PostAction.LoadPostsAction, PostListState()).subscribe(testObserver)

        val actions = testObserver.values()

        assert(actions.isNotEmpty())

        val lastAction = actions.last()

        assert(lastAction is PostAction.PostsLoadedAction)

    }
}