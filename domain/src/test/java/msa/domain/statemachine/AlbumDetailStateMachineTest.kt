package msa.domain.statemachine

import io.reactivex.Scheduler
import msa.domain.DummyData
import msa.domain.actionstate.AlbumAction
import msa.domain.actionstate.AlbumDetailState
import msa.domain.repository.Repository
import msa.domain.usecases.GetAlbumDetail
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
class AlbumDetailStateMachineTest {

    private lateinit var albumDetailStateMachine: AlbumDetailStateMachine

    @Before
    @Throws
    fun setUp() {

        val repository = mock(Repository::class.java)

        val threadExecutor = mock(Scheduler::class.java)
        val albumExecutionScheduler = mock(Scheduler::class.java)

        val getAlbumDetail = GetAlbumDetail(repository, threadExecutor, albumExecutionScheduler)

        albumDetailStateMachine = AlbumDetailStateMachine(getAlbumDetail)

    }

    @Test
    fun testReducer() {

        var state = AlbumDetailState()

        state = albumDetailStateMachine.reducer(state, AlbumAction.LoadAlbumDetailAction(1, 1))

        assertNotNull(state.albumId)
        assertNotNull(state.userId)

        assert(state.loading)

        state = albumDetailStateMachine.reducer(
            state,
            AlbumAction.AlbumDetailLoadedAction(
                album = DummyData.getAlbums().first(),
                photos = emptyList(),
                user = DummyData.getUsers().first()
            )
        )

        assertNotNull(state.album)
        assertNotNull(state.photos)

        state = albumDetailStateMachine.reducer(state, AlbumAction.RefreshAlbumsAction)

        assert(state.refreshing)

        state = albumDetailStateMachine.reducer(
            state,
            AlbumAction.AlbumDetailLoadedAction(
                album = DummyData.getAlbums().first(),
                photos = emptyList(),
                user = DummyData.getUsers().first()
            )
        )

        assertFalse(state.loading)
        assertFalse(state.refreshing)
        assertNotNull(state.album)
        assertNotNull(state.photos)

        state = albumDetailStateMachine.reducer(
            state,
            AlbumAction.ErrorLoadingAlbumsAction(Exception("Error loading album detail"))
        )

        assertFalse(state.loading)
        assertFalse(state.refreshing)
        assertNotNull(state.exception)

    }
}