package msa.domain.statemachine

import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import msa.domain.actionstate.AlbumAction
import msa.domain.actionstate.AlbumListState
import msa.domain.core.Action
import msa.domain.core.BaseStateMachine
import msa.domain.usecases.GetAlbums

/**
 * Created by Abhi Muktheeswarar.
 */

class AlbumListStateMachine(getAlbums: GetAlbums) : BaseStateMachine<AlbumListState> {

    override val input: Relay<Action> = PublishRelay.create()

    override val state: Observable<AlbumListState> = input
        .doOnNext { println("Input Action ${it.javaClass.simpleName}") }
        .reduxStore(
            initialState = AlbumListState(),
            sideEffects = listOf(
                getAlbums::loadAlbumsSideEffect,
                getAlbums::refreshAlbumsSideEffect
            ),
            reducer = ::reducer
        )
        .distinctUntilChanged()
        .doOnNext { println("RxStore state ${it.javaClass.simpleName}") }

    override fun reducer(state: AlbumListState, action: Action): AlbumListState {
        return when (action) {

            is AlbumAction.LoadingAlbumsAction -> state.copy(loading = true, refreshing = false)

            is AlbumAction.RefreshAlbumsAction -> state.copy(loading = false, refreshing = true)

            is AlbumAction.AlbumsLoadedAction -> state.copy(
                loading = false,
                refreshing = false,
                albums = action.albums,
                exception = null
            )

            is AlbumAction.ErrorLoadingAlbumsAction -> state.copy(
                loading = false,
                refreshing = false,
                exception = action.exception
            )

            else -> state
        }
    }
}