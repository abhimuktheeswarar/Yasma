package msa.yasma.album.detail

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_list.*
import msa.domain.actionstate.AlbumAction
import msa.domain.actionstate.AlbumDetailState
import msa.domain.core.Action
import msa.yasma.R
import msa.yasma.base.BaseFragment
import msa.yasma.base.BaseKotlinEpoxyModel
import msa.yasma.common.items.DetailHeaderItemModel
import msa.yasma.common.items.LoadingItemModel_
import msa.yasma.common.items.UserDetailItemModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * Created by Abhi Muktheeswarar.
 */

class AlbumDetailFragment : BaseFragment() {

    private val albumDetailViewModel by viewModel<AlbumDetailViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_list

    private val itemActionListener: (action: Action) -> Unit = { action ->


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        epoxyRecyclerView.layoutManager = GridLayoutManager(context!!, 2)
        epoxyRecyclerView.setPadding(
            resources.getDimensionPixelSize(R.dimen.eight),
            0,
            resources.getDimensionPixelSize(R.dimen.eight),
            0
        )
        epoxyRecyclerView.setItemSpacingDp(8)
        swipeRefreshLayout.isEnabled = false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        albumDetailViewModel.state.observe(this, Observer {
            setupViews(it as AlbumDetailState)
        })

        arguments?.let { bundle ->

            val args = AlbumDetailFragmentArgs.fromBundle(bundle)
            albumDetailViewModel.input.accept(
                AlbumAction.LoadAlbumDetailAction(
                    albumId = args.albumId,
                    userId = args.userId
                )
            )
        }
    }

    private fun setupViews(state: AlbumDetailState) {

        epoxyRecyclerView.buildModelsWith { controller ->


            LoadingItemModel_().id("Loading").addIf(state.loading, controller)

            state.album?.let { post ->

                DetailHeaderItemModel(title = post.title).id(DetailHeaderItemModel.ID)
                    .addTo(controller)

            }

            state.user?.let { user ->

                UserDetailItemModel(
                    name = user.name,
                    email = user.email,
                    phone = user.phone,
                    company = user.company.name
                ).id(UserDetailItemModel.ID).addTo(controller)
            }

            state.photos?.forEach { photo ->

                PhotoItemModel(
                    id = photo.id,
                    title = photo.title,
                    thumbnailUrl = photo.thumbnailUrl,
                    url = photo.url
                ).apply { this.itemActionListener = this@AlbumDetailFragment.itemActionListener }.id(photo.id)
                    .addTo(controller)
            }
        }

        state.exception?.let {
            Timber.e(it)
        }
    }
}

data class PhotoItemModel(
    private val id: Int,
    private val title: String,
    private val thumbnailUrl: String,
    private val url: String
) :
    BaseKotlinEpoxyModel(R.layout.item_photo) {

    lateinit var itemActionListener: (action: Action) -> Unit

    private val rootItemView by bind<ViewGroup>(R.id.constraintLayout_item_photo)
    private val photoImageView by bind<ImageView>(R.id.image_photo)
    private val titleTextView by bind<TextView>(R.id.text_photo_title)

    override fun bind() {

        Glide.with(photoImageView.context)
            .load(thumbnailUrl)
            .into(photoImageView)

        titleTextView.text = title

        rootItemView.setOnClickListener {
            itemActionListener(AlbumAction.OpenFullScreenImageViewerAction(photoId = id, imageUrl = url))
        }

    }

    override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
        return 1
    }
}

