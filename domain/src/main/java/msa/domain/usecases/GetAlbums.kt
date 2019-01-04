package msa.domain.usecases

import com.freeletics.rxredux.StateAccessor
import com.github.kittinunf.result.Validation
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import msa.domain.actionstate.AlbumAction
import msa.domain.actionstate.AlbumListState
import msa.domain.core.Action
import msa.domain.core.State
import msa.domain.entities.Params
import msa.domain.interactor.UseCase
import msa.domain.repository.Repository

/**
 * Created by Abhi Muktheeswarar.
 */

class GetAlbums(
    private val repository: Repository,
    threadExecutor: Scheduler,
    postExecutionScheduler: Scheduler
) : UseCase(threadExecutor, postExecutionScheduler) {

    override fun buildUseCaseObservable(action: Action, state: State): Observable<Action> {
        val params = Params(loadFromCache = action !is AlbumAction.RefreshAlbumsAction)
        return Observable.zip(
            repository.getAlbums(params),
            repository.getUsers(params),
            BiFunction { albumsResult, usersResult ->

                val validation = Validation(albumsResult, usersResult)

                if (validation.hasFailure) {

                    AlbumAction.ErrorLoadingAlbumsAction(validation.failures.first())

                } else {

                    val albums = albumsResult.get()
                    val users = usersResult.get().associateBy { it.id }

                    val albumsData = albums.map { post -> Pair(post, users[post.userId]!!) }

                    AlbumAction.AlbumsLoadedAction(albumsData)
                }

            })
    }

    fun loadAlbumsSideEffect(actions: Observable<Action>, state: StateAccessor<State>): Observable<Action> =
        actions.ofType(AlbumAction.LoadAlbumsAction::class.java)
            .filter { (state() as? AlbumListState)?.albums.isNullOrEmpty() }
            .switchMap { execute(it, state()).startWith(AlbumAction.LoadingAlbumsAction) }

    fun refreshAlbumsSideEffect(actions: Observable<Action>, state: StateAccessor<State>): Observable<Action> =
        actions.ofType(AlbumAction.RefreshAlbumsAction::class.java)
            .switchMap { execute(it, state()) }
}