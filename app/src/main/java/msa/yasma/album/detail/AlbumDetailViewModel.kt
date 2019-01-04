package msa.yasma.album.detail

import msa.domain.statemachine.AlbumDetailStateMachine
import msa.yasma.base.BaseViewModel

/**
 * Created by Abhi Muktheeswarar.
 */

class AlbumDetailViewModel(albumDetailStateMachine: AlbumDetailStateMachine) : BaseViewModel() {

    init {

        addDisposable(inputRelay.subscribe(albumDetailStateMachine.input))
        addDisposable(albumDetailStateMachine.state.subscribe { state -> mutableState.value = state })
    }
}