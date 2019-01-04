package msa.domain.usecases

import com.freeletics.rxredux.StateAccessor
import com.github.kittinunf.result.Validation
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import msa.domain.actionstate.PostAction
import msa.domain.actionstate.PostListState
import msa.domain.core.Action
import msa.domain.core.State
import msa.domain.entities.Params
import msa.domain.interactor.UseCase
import msa.domain.repository.Repository

/**
 * Created by Abhi Muktheeswarar.
 */

class GetPosts(
    private val repository: Repository,
    threadExecutor: Scheduler,
    postExecutionScheduler: Scheduler
) : UseCase(threadExecutor, postExecutionScheduler) {

    override fun buildUseCaseObservable(action: Action, state: State): Observable<Action> {
        val params = Params(loadFromCache = action !is PostAction.RefreshPostsAction)
        return Observable.zip(
            repository.getPosts(params),
            repository.getUsers(params),
            BiFunction { postsResult, usersResult ->

                val validation = Validation(postsResult, usersResult)

                if (validation.hasFailure) {

                    PostAction.ErrorLoadingPostsAction(validation.failures.first())

                } else {

                    val posts = postsResult.get()
                    val users = usersResult.get().associateBy { it.id }

                    val postsData = posts.map { post -> Pair(post, users[post.userId]!!) }

                    PostAction.PostsLoadedAction(postsData)
                }

            })
    }

    fun loadPostsSideEffect(actions: Observable<Action>, state: StateAccessor<State>): Observable<Action> =
        actions.ofType(PostAction.LoadPostsAction::class.java)
            .filter { (state() as? PostListState)?.posts.isNullOrEmpty() }
            .switchMap { execute(it, state()).startWith(PostAction.LoadingPostsAction) }

    fun refreshPostsSideEffect(actions: Observable<Action>, state: StateAccessor<State>): Observable<Action> =
        actions.ofType(PostAction.RefreshPostsAction::class.java)
            .switchMap { execute(it, state()) }
}