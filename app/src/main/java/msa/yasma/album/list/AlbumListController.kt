package msa.yasma.album.list

import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.TypedEpoxyController
import msa.domain.actionstate.AlbumListState
import msa.domain.core.Action
import msa.yasma.common.items.LoadingItemModel_
import msa.yasma.common.items.SimpleItemModel
import msa.yasma.common.items.simpleItem

/**
 * Created by Abhi Muktheeswarar.
 */

class AlbumListController(private val itemActionListener: (action: Action) -> Unit) :
    TypedEpoxyController<AlbumListState>() {

    @AutoModel
    lateinit var loadingItemModel: LoadingItemModel_

    fun setState(state: AlbumListState) {
        setData(state)
    }

    override fun buildModels(state: AlbumListState) {

        loadingItemModel.addIf(state.loading, this)

        state.albums?.forEach { (album, user) ->

            simpleItem {

                id(album.id)
                itemId(album.id)
                itemType(SimpleItemModel.SimpleItemType.ALBUM)
                title(album.title)
                name(user.name)
                itemActionListener(itemActionListener)
            }
        }

    }
}

