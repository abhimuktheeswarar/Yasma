package msa.yasma.post.list

import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.TypedEpoxyController
import msa.domain.actionstate.PostListState
import msa.domain.core.Action
import msa.yasma.common.items.LoadingItemModel_

/**
 * Created by Abhi Muktheeswarar.
 */

class PostListController(private val postItemActionListener: (action: Action) -> Unit) :
    TypedEpoxyController<PostListState>() {


    @AutoModel
    lateinit var loadingItemModel: LoadingItemModel_

    fun setState(state: PostListState) {
        setData(state)
    }

    override fun buildModels(state: PostListState) {

        loadingItemModel.addIf(state.loading, this)

        state.posts?.forEach { (post, user) ->

            postItem {

                id(post.id)
                postId(post.id)
                title(post.title)
                name(user.name)
                body(post.body)
                postItemActionListener(postItemActionListener)
            }
        }

    }
}

