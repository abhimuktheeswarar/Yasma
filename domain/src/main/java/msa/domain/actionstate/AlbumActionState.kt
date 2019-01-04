package msa.domain.actionstate

import msa.domain.core.Action
import msa.domain.core.State
import msa.domain.entities.Album
import msa.domain.entities.User

/**
 * Created by Abhi Muktheeswarar.
 */

sealed class AlbumAction : Action {

    object LoadAlbumsAction : AlbumAction()

    object RefreshAlbumsAction : AlbumAction()

    object LoadingAlbumsAction : AlbumAction()

    data class AlbumsLoadedAction(val albums: List<Pair<Album, User>>) : AlbumAction()

    data class ErrorLoadingAlbumsAction(val exception: Exception) : AlbumAction()

}

data class AlbumListState(
    val loading: Boolean = true,
    val refreshing: Boolean = false,
    val albums: List<Pair<Album, User>>? = null,
    val exception: Exception? = null
) : State