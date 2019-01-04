package msa.domain.statemachine

import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import msa.domain.actionstate.PostAction
import msa.domain.actionstate.PostDetailState
import msa.domain.core.Action
import msa.domain.core.BaseStateMachine
import msa.domain.usecases.GetPostDetail

/**
 * Created by Abhi Muktheeswarar.
 */

class PostDetailStateMachine(getPostDetail: GetPostDetail) : BaseStateMachine<PostDetailState> {

    override val input: Relay<Action> = PublishRelay.create()

    override val state: Observable<PostDetailState> = input
        .doOnNext { println("Input Action ${it.javaClass.simpleName}") }
        .reduxStore(
            initialState = PostDetailState(),
            sideEffects = listOf(
                getPostDetail::loadPostDetailSideEffect,
                getPostDetail::refreshPostDetailSideEffect
            ),
            reducer = ::reducer
        )
        .distinctUntilChanged()
        .doOnNext { println("RxStore state ${it.javaClass.simpleName}") }

    override fun reducer(state: PostDetailState, action: Action): PostDetailState {
        return when (action) {

            is PostAction.LoadPostDetailAction -> state.copy(postId = action.postId, userId = action.userId)

            is PostAction.LoadingPostsAction -> state.copy(loading = true, refreshing = false)

            is PostAction.RefreshPostsAction -> state.copy(loading = false, refreshing = true)

            is PostAction.PostDetailLoadedAction -> state.copy(
                loading = false,
                refreshing = false,
                post = action.post,
                comments = action.comments,
                user = action.user,
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