package msa.domain.statemachine

import io.reactivex.Scheduler
import msa.domain.actionstate.AlbumAction
import msa.domain.actionstate.AlbumListState
import msa.domain.repository.Repository
import msa.domain.usecases.GetAlbums
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
class AlbumListStateMachineTest {

    private lateinit var albumListStateMachine: AlbumListStateMachine

    @Before
    @Throws
    fun setUp() {

        val repository = mock(Repository::class.java)

        val threadExecutor = mock(Scheduler::class.java)
        val albumExecutionScheduler = mock(Scheduler::class.java)

        val getAlbums = GetAlbums(repository, threadExecutor, albumExecutionScheduler)

        albumListStateMachine = AlbumListStateMachine(getAlbums)

    }

    @Test
    fun testReducer() {

        var state = AlbumListState()

        state = albumListStateMachine.reducer(state, AlbumAction.LoadAlbumsAction)

        assert(state.loading)

        state = albumListStateMachine.reducer(state, AlbumAction.AlbumsLoadedAction(emptyList()))

        assertNotNull(state.albums)

        state = albumListStateMachine.reducer(state, AlbumAction.RefreshAlbumsAction)

        assert(state.refreshing)

        state = albumListStateMachine.reducer(state, AlbumAction.AlbumsLoadedAction(emptyList()))

        assertFalse(state.loading)
        assertFalse(state.refreshing)
        assertNotNull(state.albums)

        state = albumListStateMachine.reducer(
            state,
            AlbumAction.ErrorLoadingAlbumsAction(Exception("Error loading albums"))
        )

        assertFalse(state.loading)
        assertFalse(state.refreshing)
        assertNotNull(state.exception)

    }
}