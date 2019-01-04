package msa.yasma.post.detail

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_list.*
import msa.domain.actionstate.PostAction
import msa.domain.actionstate.PostDetailState
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

class PostDetailFragment : BaseFragment() {


    private val postDetailViewModel by viewModel<PostDetailViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        epoxyRecyclerView.setItemSpacingDp(8)
        swipeRefreshLayout.isEnabled = false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        postDetailViewModel.state.observe(this, Observer {
            setupViews(it as PostDetailState)
        })

        arguments?.let { bundle ->

            val args = PostDetailFragmentArgs.fromBundle(bundle)
            postDetailViewModel.input.accept(
                PostAction.LoadPostDetailAction(
                    postId = args.postId,
                    userId = args.userId
                )
            )
        }
    }

    private fun setupViews(state: PostDetailState) {

        epoxyRecyclerView.buildModelsWith { controller ->


            LoadingItemModel_().id("Loading").addIf(state.loading, controller)

            state.post?.let { post ->

                DetailHeaderItemModel(title = post.title, body = post.body).id(DetailHeaderItemModel.ID)
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

            state.comments?.forEach { comment ->

                PostCommentItemModel(
                    id = comment.id,
                    name = comment.name,
                    email = comment.email,
                    body = comment.body
                ).id(comment.id).addTo(controller)

            }
        }

        state.exception?.let {
            Timber.e(it)
        }

    }
}

data class PostCommentItemModel(
    private val id: Int,
    private val name: String,
    private val email: String,
    private val body: String
) :
    BaseKotlinEpoxyModel(R.layout.item_post_comment) {

    private val nameTextView by bind<TextView>(R.id.text_name)
    private val emailTextView by bind<TextView>(R.id.text_email)
    private val bodyTextView by bind<TextView>(R.id.text_body)

    override fun bind() {

        nameTextView.text = name
        emailTextView.text = email
        bodyTextView.text = body

    }
}

