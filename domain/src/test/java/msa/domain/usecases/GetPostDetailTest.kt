package msa.domain.usecases

import com.github.kittinunf.result.Result
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.observers.TestObserver
import msa.domain.DummyData
import msa.domain.actionstate.PostAction
import msa.domain.actionstate.PostDetailState
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
class GetPostDetailTest {

    private lateinit var repository: Repository
    private lateinit var getPostDetail: GetPostDetail

    @Before
    @Throws
    fun setUp() {

        val post = DummyData.getPosts().first()
        val comments = DummyData.getPostComments()
        val user = DummyData.getUsers().first()

        repository = mock(Repository::class.java)

        val threadExecutor = mock(Scheduler::class.java)
        val postExecutionScheduler = mock(Scheduler::class.java)

        getPostDetail = GetPostDetail(repository, threadExecutor, postExecutionScheduler)

        val paramsT = Params(true, 1)
        val paramsF = Params(true, 1)

        `when`(repository.getPostDetail(paramsT)).thenReturn(Observable.just(Result.of(post)))
        `when`(repository.getPostComments(paramsT)).thenReturn(Observable.just(Result.of(comments)))
        `when`(repository.getUserDetail(paramsT)).thenReturn(Observable.just(Result.of(user)))

        `when`(repository.getPostDetail(paramsF)).thenReturn(Observable.just(Result.of(post)))
        `when`(repository.getPostComments(paramsF)).thenReturn(Observable.just(Result.of(comments)))
        `when`(repository.getUserDetail(paramsF)).thenReturn(Observable.just(Result.of(user)))

    }

    @Test
    fun testGetPosts() {

        val testObserver = TestObserver<Action>()

        getPostDetail.buildUseCaseObservable(PostAction.LoadPostDetailAction(1, 1), PostDetailState())
            .subscribe(testObserver)

        val actions = testObserver.values()

        assert(actions.isNotEmpty())

        val lastAction = actions.last()

        assert(lastAction is PostAction.PostDetailLoadedAction)

    }
}