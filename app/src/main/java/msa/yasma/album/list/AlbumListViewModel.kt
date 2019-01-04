package msa.yasma.album.list

import msa.domain.statemachine.AlbumListStateMachine
import msa.yasma.base.BaseViewModel

/**
 * Created by Abhi Muktheeswarar.
 */

class AlbumListViewModel(albumListStateMachine: AlbumListStateMachine) : BaseViewModel() {

    init {

        addDisposable(inputRelay.subscribe(albumListStateMachine.input))
        addDisposable(albumListStateMachine.state.subscribe { state -> mutableState.value = state })
    }
}