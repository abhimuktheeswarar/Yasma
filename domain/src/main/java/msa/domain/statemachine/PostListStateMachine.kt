package msa.domain.statemachine

import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import msa.domain.actionstate.PostAction
import msa.domain.actionstate.PostListState
import msa.domain.core.Action
import msa.domain.core.BaseStateMachine
import msa.domain.usecases.GetPosts

/**
 * Created by Abhi Muktheeswarar.
 */

class PostListStateMachine(getPosts: GetPosts) : BaseStateMachine<PostListState> {

    override val input: Relay<Action> = PublishRelay.create()

    override val state: Observable<PostListState> = input
        .doOnNext { println("Input Action ${it.javaClass.simpleName}") }
        .reduxStore(
            initialState = PostListState(),
            sideEffects = listOf(
                getPosts::loadPostsSideEffect,
                getPosts::refreshPostsSideEffect
            ),
            reducer = ::reducer
        )
        .distinctUntilChanged()
        .doOnNext { println("RxStore state ${it.javaClass.simpleName}") }

    override fun reducer(state: PostListState, action: Action): PostListState {
        return when (action) {

            is PostAction.LoadingPostsAction -> state.copy(loading = true, refreshing = false)

            is PostAction.RefreshPostsAction -> state.copy(loading = false, refreshing = true)

            is PostAction.PostsLoadedAction -> state.copy(
                loading = false,
                refreshing = false,
                posts = action.posts,
                exception = null
            )

            is PostAction.ErrorLoadingPostsAction -> state.copy(
                loading = false,
                refreshing = false,
                exception = action.exception
            )

            else -> state
        }
    }
}