package msa.yasma.album.list

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_list.*
import msa.domain.actionstate.AlbumAction
import msa.domain.actionstate.AlbumListState
import msa.yasma.R
import msa.yasma.album.detail.AlbumDetailFragmentDirections
import msa.yasma.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

/**
 * Created by Abhi Muktheeswarar.
 */

class AlbumListFragment : BaseFragment() {

    private val albumListViewModel by sharedViewModel<AlbumListViewModel>()
    private val albumListController by lazy {
        AlbumListController { action ->

            if (action is AlbumAction.LoadAlbumDetailAction) {

                findNavController().navigate(
                    AlbumDetailFragmentDirections.navigateToAlbumDetail(
                        action.albumId,
                        action.userId
                    )
                )
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        epoxyRecyclerView.setController(albumListController)
        epoxyRecyclerView.setItemSpacingDp(8)
        swipeRefreshLayout.setOnRefreshListener { albumListViewModel.input.accept(AlbumAction.RefreshAlbumsAction) }
        swipeRefreshLayout.isEnabled = false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        albumListViewModel.state.observe(this, Observer {
            setupViews(it as AlbumListState)
        })

        albumListViewModel.input.accept(AlbumAction.LoadAlbumsAction)
    }

    private fun setupViews(state: AlbumListState) {

        albumListController.setState(state)

        state.exception?.let {
            Timber.e(it)
        }

        swipeRefreshLayout.isEnabled = !state.albums.isNullOrEmpty()
        swipeRefreshLayout.isEnabled = !state.refreshing
        swipeRefreshLayout.isRefreshing = state.refreshing

    }
}