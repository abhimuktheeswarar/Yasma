package msa.yasma.album.imageviewer

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_imageviewer.*
import msa.yasma.R
import msa.yasma.base.BaseFragment

/**
 * Created by Abhi Muktheeswarar.
 */

class ImageViewerFragment : BaseFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_imageviewer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { bundle ->

            val args = ImageViewerFragmentArgs.fromBundle(bundle)

            val imageUrl = args.url

            Glide.with(context!!).load(imageUrl).into(image_fullSize)
        }

    }
}