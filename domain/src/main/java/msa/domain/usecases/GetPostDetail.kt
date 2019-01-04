package msa.domain.usecases

import com.freeletics.rxredux.StateAccessor
import com.github.kittinunf.result.Validation
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.Function3
import msa.domain.actionstate.PostAction
import msa.domain.actionstate.PostDetailState
import msa.domain.core.Action
import msa.domain.core.State
import msa.domain.entities.Params
import msa.domain.interactor.UseCase
import msa.domain.repository.Repository

/**
 * Created by Abhi Muktheeswarar.
 */

class GetPostDetail(
    private val repository: Repository,
    threadExecutor: Scheduler,
    postExecutionScheduler: Scheduler
) : UseCase(threadExecutor, postExecutionScheduler) {

    override fun buildUseCaseObservable(action: Action, state: State): Observable<Action> {
        val loadFromCache = action !is PostAction.RefreshPostsAction
        val postId = (action as? PostAction.LoadPostDetailAction)?.postId ?: (state as PostDetailState).postId!!
        val userId = (action as? PostAction.LoadPostDetailAction)?.userId ?: (state as PostDetailState).userId!!
        val paramsForPost = Params(loadFromCache = loadFromCache, id = postId)
        val paramsForUser = Params(loadFromCache = loadFromCache, id = userId)
        return Observable.zip(
            repository.getPostDetail(paramsForPost),
            repository.getPostComments(paramsForPost),
            repository.getUserDetail(paramsForUser),
            Function3 { postDetailResult, postCommentsResult, usersResult ->

                val validation = Validation(postDetailResult, postCommentsResult, usersResult)

                if (validation.hasFailure) {

                    PostAction.ErrorLoadingPostsAction(validation.failures.first())

                } else {

                    val post = postDetailResult.get()
                    val user = usersResult.get()
                    val postComments = postCommentsResult.get()

                    PostAction.PostDetailLoadedAction(post = post, comments = postComments, user = user)
                }

            })
    }

    fun loadPostDetailSideEffect(actions: Observable<Action>, state: StateAccessor<State>): Observable<Action> =
        actions.ofType(PostAction.LoadPostDetailAction::class.java)
            .filter { (state() as? PostDetailState)?.post == null || (state() as? PostDetailState)?.comments.isNullOrEmpty() }
            .switchMap { execute(it, state()).startWith(PostAction.LoadingPostsAction) }

    fun refreshPostDetailSideEffect(actions: Observable<Action>, state: StateAccessor<State>): Observable<Action> =
        actions.ofType(PostAction.RefreshPostsAction::class.java)
            .switchMap { execute(it, state()) }
}