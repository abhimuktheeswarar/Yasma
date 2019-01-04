package msa.domain.usecases

import com.github.kittinunf.result.Result
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.observers.TestObserver
import msa.domain.DummyData
import msa.domain.actionstate.AlbumAction
import msa.domain.actionstate.AlbumDetailState
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
class GetAlbumDetailTest {

    private lateinit var repository: Repository
    private lateinit var getAlbumDetail: GetAlbumDetail

    @Before
    @Throws
    fun setUp() {

        val album = DummyData.getAlbums().first()
        val photos = DummyData.getPhotos()
        val user = DummyData.getUsers().first()

        repository = mock(Repository::class.java)

        val threadExecutor = mock(Scheduler::class.java)
        val postExecutionScheduler = mock(Scheduler::class.java)

        getAlbumDetail = GetAlbumDetail(repository, threadExecutor, postExecutionScheduler)

        val paramsT = Params(true, 1)
        val paramsF = Params(true, 1)

        `when`(repository.getAlbumDetail(paramsT)).thenReturn(Observable.just(Result.of(album)))
        `when`(repository.getAlbumPhotos(paramsT)).thenReturn(Observable.just(Result.of(photos)))
        `when`(repository.getUserDetail(paramsT)).thenReturn(Observable.just(Result.of(user)))

        `when`(repository.getAlbumDetail(paramsF)).thenReturn(Observable.just(Result.of(album)))
        `when`(repository.getAlbumPhotos(paramsF)).thenReturn(Observable.just(Result.of(photos)))
        `when`(repository.getUserDetail(paramsF)).thenReturn(Observable.just(Result.of(user)))

    }

    @Test
    fun testGetAlbumDetail() {

        val testObserver = TestObserver<Action>()

        getAlbumDetail.buildUseCaseObservable(AlbumAction.LoadAlbumDetailAction(1, 1), AlbumDetailState())
            .subscribe(testObserver)

        val actions = testObserver.values()

        assert(actions.isNotEmpty())

        val lastAction = actions.last()

        assert(lastAction is AlbumAction.AlbumDetailLoadedAction)

    }
}