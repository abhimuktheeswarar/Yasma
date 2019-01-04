package msa.domain.usecases

import com.github.kittinunf.result.Result
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.observers.TestObserver
import msa.domain.DummyData
import msa.domain.actionstate.AlbumAction
import msa.domain.actionstate.AlbumListState
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
class GetAlbumsTest {

    private lateinit var repository: Repository
    private lateinit var getAlbums: GetAlbums

    @Before
    @Throws
    fun setUp() {

        val albums = DummyData.getAlbums()
        val users = DummyData.getUsers()

        repository = mock(Repository::class.java)

        val threadExecutor = mock(Scheduler::class.java)
        val postExecutionScheduler = mock(Scheduler::class.java)

        getAlbums = GetAlbums(repository, threadExecutor, postExecutionScheduler)


        `when`(repository.getAlbums(Params(true))).thenReturn(Observable.just(Result.of(albums)))
        `when`(repository.getUsers(Params(true))).thenReturn(Observable.just(Result.of(users)))

    }

    @Test
    fun testGetAlbums() {

        val testObserver = TestObserver<Action>()

        getAlbums.buildUseCaseObservable(AlbumAction.LoadAlbumsAction, AlbumListState()).subscribe(testObserver)

        val actions = testObserver.values()

        assert(actions.isNotEmpty())

        val lastAction = actions.last()

        assert(lastAction is AlbumAction.AlbumsLoadedAction)

    }
}