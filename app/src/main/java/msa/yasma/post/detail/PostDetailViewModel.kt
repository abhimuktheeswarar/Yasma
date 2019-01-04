package msa.yasma.post.detail

import msa.domain.statemachine.PostDetailStateMachine
import msa.yasma.base.BaseViewModel

/**
 * Created by Abhi Muktheeswarar.
 */

class PostDetailViewModel(postDetailStateMachine: PostDetailStateMachine) : BaseViewModel() {

    init {

        addDisposable(inputRelay.subscribe(postDetailStateMachine.input))
        addDisposable(postDetailStateMachine.state.subscribe { state -> mutableState.value = state })
    }
}