package msa.domain.actionstate

import msa.domain.core.Action
import msa.domain.core.State
import msa.domain.entities.Post
import msa.domain.entities.User

/**
 * Created by Abhi Muktheeswarar.
 */

sealed class PostAction : Action {

    object LoadPostsAction : PostAction()

    object RefreshPostsAction : PostAction()

    object LoadingPostsAction : PostAction()

    data class PostsLoadedAction(val posts: List<Pair<Post, User>>) : PostAction()

    data class ErrorLoadingPostsAction(val exception: Exception) : PostAction()

}

data class PostListState(
    val loading: Boolean = true,
    val refreshing: Boolean = false,
    val posts: List<Pair<Post, User>>? = null,
    val exception: Exception? = null
) : State