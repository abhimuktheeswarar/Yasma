package msa.yasma.post.list

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_list.*
import msa.domain.actionstate.PostAction
import msa.domain.actionstate.PostListState
import msa.yasma.R
import msa.yasma.base.BaseFragment
import msa.yasma.post.detail.PostDetailFragmentDirections
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

/**
 * Created by Abhi Muktheeswarar.
 */

class PostListFragment : BaseFragment() {

    private val postListViewModel by sharedViewModel<PostListViewModel>()
    private val postListController by lazy {
        PostListController { action ->

            if (action is PostAction.LoadPostDetailAction) {

                findNavController().navigate(
                    PostDetailFragmentDirections.navigateToPostDetail(
                        action.postId,
                        action.userId
                    )
                )
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        epoxyRecyclerView.setController(postListController)
        epoxyRecyclerView.setItemSpacingDp(8)
        swipeRefreshLayout.setOnRefreshListener { postListViewModel.input.accept(PostAction.RefreshPostsAction) }
        swipeRefreshLayout.isEnabled = false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        postListViewModel.state.observe(this, Observer {
            setupViews(it as PostListState)
        })

        postListViewModel.input.accept(PostAction.LoadPostsAction)
    }

    private fun setupViews(state: PostListState) {

        postListController.setState(state)

        state.exception?.let {
            Timber.e(it)
        }

        swipeRefreshLayout.isEnabled = !state.posts.isNullOrEmpty()
        swipeRefreshLayout.isEnabled = !state.refreshing
        swipeRefreshLayout.isRefreshing = state.refreshing

    }
}