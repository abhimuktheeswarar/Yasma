package msa.domain.usecases

import com.freeletics.rxredux.StateAccessor
import com.github.kittinunf.result.Validation
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.Function3
import msa.domain.actionstate.AlbumAction
import msa.domain.actionstate.AlbumDetailState
import msa.domain.core.Action
import msa.domain.core.State
import msa.domain.entities.Params
import msa.domain.interactor.UseCase
import msa.domain.repository.Repository

/**
 * Created by Abhi Muktheeswarar.
 */

class GetAlbumDetail(
    private val repository: Repository,
    threadExecutor: Scheduler,
    postExecutionScheduler: Scheduler
) : UseCase(threadExecutor, postExecutionScheduler) {

    override fun buildUseCaseObservable(action: Action, state: State): Observable<Action> {
        val loadFromCache = action !is AlbumAction.RefreshAlbumsAction
        val albumId = (action as? AlbumAction.LoadAlbumDetailAction)?.albumId ?: (state as AlbumDetailState).albumId!!
        val userId = (action as? AlbumAction.LoadAlbumDetailAction)?.userId ?: (state as AlbumDetailState).userId!!
        val paramsForAlbum = Params(loadFromCache = loadFromCache, id = albumId)
        val paramsForUser = Params(loadFromCache = loadFromCache, id = userId)
        return Observable.zip(
            repository.getAlbumDetail(paramsForAlbum),
            repository.getAlbumPhotos(paramsForAlbum),
            repository.getUserDetail(paramsForUser),
            Function3 { albumDetailResult, albumPhotosResult, usersResult ->

                val validation = Validation(albumDetailResult, albumPhotosResult, usersResult)

                if (validation.hasFailure) {

                    AlbumAction.ErrorLoadingAlbumsAction(validation.failures.first())

                } else {

                    val album = albumDetailResult.get()
                    val user = usersResult.get()
                    val albumPhotos = albumPhotosResult.get()

                    AlbumAction.AlbumDetailLoadedAction(album = album, photos = albumPhotos, user = user)
                }

            })
    }

    fun loadAlbumDetailSideEffect(actions: Observable<Action>, state: StateAccessor<State>): Observable<Action> =
        actions.ofType(AlbumAction.LoadAlbumDetailAction::class.java)
            .filter { (state() as? AlbumDetailState)?.album == null || (state() as? AlbumDetailState)?.photos.isNullOrEmpty() }
            .switchMap { execute(it, state()).startWith(AlbumAction.LoadingAlbumsAction) }

    fun refreshAlbumDetailSideEffect(actions: Observable<Action>, state: StateAccessor<State>): Observable<Action> =
        actions.ofType(AlbumAction.RefreshAlbumsAction::class.java)
            .switchMap { execute(it, state()) }
}