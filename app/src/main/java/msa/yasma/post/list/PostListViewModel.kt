package msa.yasma.post.list

import msa.domain.statemachine.PostListStateMachine
import msa.yasma.base.BaseViewModel

/**
 * Created by Abhi Muktheeswarar.
 */

class PostListViewModel(postListStateMachine: PostListStateMachine) : BaseViewModel() {

    init {

        addDisposable(inputRelay.subscribe(postListStateMachine.input))
        addDisposable(postListStateMachine.state.subscribe { state -> mutableState.value = state })
    }
}