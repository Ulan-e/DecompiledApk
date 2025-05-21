package kz.sapasoft.emark.app.ui.photo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import com.example.decompiledapk.R
import kz.sapasoft.emark.app.domain.model.ImageDataModel
import kz.sapasoft.emark.app.ui.CustomBindingAdapterKt
import kz.sapasoft.emark.app.ui.MainActivity
import kz.sapasoft.emark.app.ui.base.DaggerFragmentExtended

class PhotoViewFragment : DaggerFragmentExtended() {

    private var mImageModel: ImageDataModel? = null
    private var rootView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mImageModel = requireArguments().getSerializable("imageModel") as? ImageDataModel
        rootView = layoutInflater.inflate(
            com.example.decompiledapk.R.layout.fragment_photo_view,
            container,
            false
        )
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        showImage(view)
    }

    private fun initView(view: View) {
        (view.findViewById<TextView>(R.id.tv_toolbar)!!).text = getString(R.string.photo)
        (activity as? MainActivity)?.setSupportActionBar(view.findViewById(R.id.toolbar) as Toolbar)
    }

    private fun showImage(view: View) {
        val imageView: ImageView = view.findViewById(R.id.iv_photo_view)
        CustomBindingAdapterKt.loadImage(imageView, mImageModel)
    }

    companion object {
        fun newInstance(imageDataModel: ImageDataModel): PhotoViewFragment {
            return PhotoViewFragment().apply {
                arguments = bundleOf(
                    "imageModel" to imageDataModel
                )
            }
        }
    }
}