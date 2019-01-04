package msa.domain.statemachine

import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import msa.domain.actionstate.AlbumAction
import msa.domain.actionstate.AlbumDetailState
import msa.domain.core.Action
import msa.domain.core.BaseStateMachine
import msa.domain.usecases.GetAlbumDetail

/**
 * Created by Abhi Muktheeswarar.
 */

class AlbumDetailStateMachine(getAlbumDetail: GetAlbumDetail) : BaseStateMachine<AlbumDetailState> {

    override val input: Relay<Action> = PublishRelay.create()

    override val state: Observable<AlbumDetailState> = input
        .doOnNext { println("Input Action ${it.javaClass.simpleName}") }
        .reduxStore(
            initialState = AlbumDetailState(),
            sideEffects = listOf(
                getAlbumDetail::loadAlbumDetailSideEffect,
                getAlbumDetail::refreshAlbumDetailSideEffect
            ),
            reducer = ::reducer
        )
        .distinctUntilChanged()
        .doOnNext { println("RxStore state ${it.javaClass.simpleName}") }

    override fun reducer(state: AlbumDetailState, action: Action): AlbumDetailState {
        return when (action) {

            is AlbumAction.LoadAlbumDetailAction -> state.copy(albumId = action.albumId, userId = action.userId)

            is AlbumAction.LoadingAlbumsAction -> state.copy(loading = true, refreshing = false)

            is AlbumAction.RefreshAlbumsAction -> state.copy(loading = false, refreshing = true)

            is AlbumAction.AlbumDetailLoadedAction -> state.copy(
                loading = false,
                refreshing = false,
                album = action.album,
                photos = action.photos,
                user = action.user,
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