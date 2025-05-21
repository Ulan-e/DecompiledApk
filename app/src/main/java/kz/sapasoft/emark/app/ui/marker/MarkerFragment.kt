package kz.sapasoft.emark.app.ui.marker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import com.example.decompiledapk.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kz.sapasoft.emark.app.domain.model.FieldModel
import kz.sapasoft.emark.app.domain.model.ImageDataModel
import kz.sapasoft.emark.app.domain.model.MarkerModel
import kz.sapasoft.emark.app.domain.model.ProjectModel
import kz.sapasoft.emark.app.domain.model.TagModel
import kz.sapasoft.emark.app.domain.model.TemplateModel
import kz.sapasoft.emark.app.ui.MainActivity
import kz.sapasoft.emark.app.ui.base.DaggerFragmentExtended
import kz.sapasoft.emark.app.ui.custom_views.FieldView
import kz.sapasoft.emark.app.ui.custom_views.FieldViewDate
import kz.sapasoft.emark.app.ui.custom_views.FieldViewDouble
import kz.sapasoft.emark.app.ui.custom_views.FieldViewLong
import kz.sapasoft.emark.app.ui.custom_views.FieldViewSpinner
import kz.sapasoft.emark.app.ui.custom_views.FieldViewText
import kz.sapasoft.emark.app.ui.custom_views.MarkerDepthView
import kz.sapasoft.emark.app.ui.custom_views.MarkerIdentifierView
import kz.sapasoft.emark.app.ui.custom_views.MarkerModelView
import kz.sapasoft.emark.app.ui.custom_views.MarkerPhotoView
import kz.sapasoft.emark.app.ui.custom_views.MarkerTypeView
import kz.sapasoft.emark.app.ui.photo.OnPhotoViewListener
import kz.sapasoft.emark.app.ui.photo.PhotoViewFragment
import kz.sapasoft.emark.app.utils.Constants
import kz.sapasoft.emark.app.utils.showToast
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.MediaFile
import pl.aprilapps.easyphotopicker.MediaSource
import java.util.UUID
import javax.inject.Inject
import kotlin.jvm.internal.Intrinsics

class MarkerFragment : DaggerFragmentExtended(), OnFieldValueChangeListener, OnMarkerTypeChangeListener, OnImageClickListener, OnPhotoViewListener {

    private val REQUEST_CAMERA_PERMISSION = 1
    private val TAG: String = this::class.java.simpleName
    private val localImageList = ArrayList<ImageDataModel>()
    private val mFieldViewList = ArrayList<FieldView>()
    private var mMarkerModel: MarkerModel? = null
    private var mOnMarkerChangeListener: OnMarkerChangeListener? = null
    private var mProjectModel: ProjectModel? = null
    private var mTagList: List<TagModel>? = null
    private var mTemplateList: List<TemplateModel>? = null

    val viewModel: MarkerViewModel
        /* access modifiers changed from: private */
        get() = `viewModel$delegate` as MarkerViewModel

    private var rootView: View? = null
    private var easyImage: EasyImage? = null


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val `viewModel$delegate`: MarkerViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(MarkerViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mProjectModel = requireArguments().getSerializable("projectModel") as? ProjectModel
        mMarkerModel = requireArguments().getSerializable("marker") as? MarkerModel
        rootView =  layoutInflater.inflate(R.layout.fragment_marker, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(mMarkerModel != null) {
            viewModel.getAllData(mMarkerModel!!, mProjectModel!!.markerTemplateIds)
        }
        initView(view)
        setListeners(view)
        setObservers()

        easyImage = EasyImage.Builder(requireContext()).setCopyImagesToPublicGalleryFolder(false).allowMultiple(false).build();

    }

    private fun initView(view: View) {
        (view.findViewById(R.id.tv_toolbar) as TextView).text = getString(R.string.marker)
        (activity as? MainActivity)?.setSupportActionBar(view.findViewById(R.id.toolbar) as Toolbar)
        if (mMarkerModel?.status == null) {
            (view.findViewById(R.id.btn_save) as MaterialButton).visibility = View.GONE
        }
        // эти поля должны быть не редатируемыми
//        else if( mMarkerModel?.status == Constants.MarkerStatus.NEW){
//            view.findViewById<MarkerIdentifierView>(R.id.view_marker_identifier).enableEdit(true)
//            view.findViewById<MarkerModelView>(R.id.view_marker_model).enableEdit(true)
//        }

        requireView().findViewById<MarkerPhotoView>(R.id.view_marker_photo)
            .setOnPhotoClickListener(this)
    }

    private fun setListeners(view: View) {
        (view.findViewById(R.id.btn_save) as MaterialButton).setOnClickListener {
            val newMarkerModel = constructNewMarkerModel()
            if (newMarkerModel.status == null) {
                newMarkerModel.status = Constants.MarkerStatus.EDITED
            }
            val identifierView = rootView?.findViewById<MarkerIdentifierView>(R.id.view_marker_identifier)
            if(identifierView?.isIdentifierEmpty() == true) {
                requireContext().showToast("Идентификатор не должен быть пустым")
            }else {
                viewModel.saveMarkerAndImage(newMarkerModel, constructImageDataModel())
            }
        }
    }

    fun constructImageDataModel(): List<ImageDataModel> {
        val changedModelList = requireView().findViewById<MarkerPhotoView>(R.id.view_marker_photo).changedModelList

        mMarkerModel?.let { markerModel ->
            changedModelList.forEach { it.localIdParent = markerModel.idLocal }
        } ?: throw UninitializedPropertyAccessException("mMarkerModel")

        return changedModelList
    }

    fun fillMain(markerModel: MarkerModel) {
        val onFieldValueChangeListener: OnFieldValueChangeListener = this
        (rootView?.findViewById(R.id.view_marker_identifier) as MarkerIdentifierView).setData(
            markerModel.generalId,
            onFieldValueChangeListener
        )

        if (mMarkerModel?.status == Constants.MarkerStatus.NEW) {
            (rootView?.findViewById(R.id.view_marker_model) as MarkerModelView).setData("${markerModel.markerModel}")
        } else {
            (rootView?.findViewById(R.id.view_marker_model) as MarkerModelView).setData(
                markerModel.markerModel
            )
        }

        (rootView?.findViewById(R.id.view_marker_depth) as MarkerDepthView).setData(
            markerModel.depth,
            onFieldValueChangeListener
        )
    }

    fun fillFields(list: List<FieldModel>?) {
        var obj: Any?
        if (list != null) {
            for (next in list) {
                val it: Iterator<*> = mFieldViewList.iterator()
                while (true) {
                    if (!it.hasNext()) {
                        obj = null
                        break
                    }
                    obj = it.next()
                    if (Intrinsics.areEqual(
                            (obj as FieldView).getFieldModel().id as Any?,
                            next.id as Any?
                        )
                    ) {
                        break
                    }
                }
                val fieldView = obj as FieldView?
                fieldView?.setFieldModel(next)
            }
        }
    }

    private fun setObservers() {
        viewModel.markerModelData.observe(viewLifecycleOwner) { markerModel ->
            markerModel?.let {
                // Сохраняем модель в переменной фрагмента
                mMarkerModel = it

                // Показываем ScrollView
                val scrollView = requireView().findViewById<ScrollView>(R.id.sv_main)
                scrollView.visibility = View.VISIBLE

                // Скрываем ProgressBar
                val progressBar = requireView().findViewById<ProgressBar>(R.id.pb_main)
                progressBar.visibility = View.GONE

                // Заполняем основной интерфейс
                fillMain(it)
                fillFields(it.fields)
            }
        }
        viewModel.imagesData.observe(viewLifecycleOwner) { list ->
            list?.let {
                val markerPhotoView = requireView().findViewById<MarkerPhotoView>(R.id.view_marker_photo)
                markerPhotoView.setImageDataModelList(it)
            }
        }


        viewModel.localImagesData.observe(viewLifecycleOwner) { list ->
            list?.let {
                // Добавляем данные в локальный список
                localImageList.addAll(it)

                // Обновляем MarkerPhotoView
                val markerPhotoView = requireView().findViewById<MarkerPhotoView>(R.id.view_marker_photo)
                markerPhotoView.setImageDataModelList(it)
            }
        }
        viewModel.error.observe(viewLifecycleOwner) { error ->
            // Показываем ScrollView
            val scrollView = requireView().findViewById<ScrollView>(R.id.sv_main)
            scrollView.visibility = View.VISIBLE

            // Скрываем ProgressBar
            val progressBar = requireView().findViewById<ProgressBar>(R.id.pb_main)
            progressBar.visibility = View.GONE

            // Показываем Snackbar с сообщением об ошибке
            val toolbar = requireView().findViewById<Toolbar>(R.id.toolbar)
            Snackbar.make(
                toolbar,
                error?.toString() ?: "Unknown error",
                Snackbar.LENGTH_SHORT
            ).show()
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading == false) { // Проверяем, что загрузка завершена
                // Показываем ScrollView
                val scrollView = requireView().findViewById<ScrollView>(R.id.sv_main)
                scrollView.visibility = View.VISIBLE

                // Скрываем ProgressBar
                val progressBar = requireView().findViewById<ProgressBar>(R.id.pb_main)
                progressBar.visibility = View.GONE
            }
        }
        viewModel.markerChangeTask.observe(viewLifecycleOwner) { isChanged ->
            if (isChanged == true) {
                mOnMarkerChangeListener?.onMarkerChange()
                activity?.onBackPressed()
            }
        }
        viewModel.tagModelData.observe(viewLifecycleOwner) { tagList ->
            tagList?.let {
                mTagList = it
            }
        }
        viewModel.templateModelData.observe(viewLifecycleOwner) { templateList ->
            templateList?.let { list ->
                // Обновляем mTemplateList
                mTemplateList = list

                // Находим TemplateModel с совпадающим id
                val templateModel = list.firstOrNull {
                    it.id == mMarkerModel?.templateId
                }

                // Рисуем основной список
                drawMainList(list)

                // Рисуем список полей
                val fields = templateModel?.fields ?: list.firstOrNull()?.fields
                fields?.let { drawFieldList(it) }
            }
        }
    }

    private fun drawMainList(list: List<TemplateModel>) {
        val markerTypeView = rootView?.findViewById(R.id.view_marker_type) as MarkerTypeView
        val templateId = mMarkerModel?.templateId ?: list.first().id
        markerTypeView.setData(list, templateId, this)

        rootView?.findViewById<MarkerIdentifierView>(R.id.view_marker_identifier)?.let { view ->
            (view as MarkerIdentifierView).setData(mMarkerModel?.generalId ?: "", this)
        }

        rootView?.findViewById<MarkerModelView>(R.id.view_marker_model)?.let { view ->
            (view as MarkerModelView).setData(mMarkerModel?.markerModel ?: "")
        }

        (rootView?.findViewById<MarkerDepthView>(R.id.view_marker_depth) as MarkerDepthView).setData(null, this)
        (rootView?.findViewById<MarkerPhotoView>(R.id.view_marker_photo) as MarkerPhotoView).setListeners(this, this)
    }

    private fun drawFieldList(list: List<FieldModel>) {
        (rootView?.findViewById<LinearLayout>(R.id.ll_additional_fields) as LinearLayout).removeAllViews()
        mFieldViewList.clear()
        list.forEach { field ->
            val fieldView = drawField(field)
            mFieldViewList.add(fieldView)
            (rootView?.findViewById<LinearLayout>(R.id.ll_additional_fields) as LinearLayout).addView(fieldView)
        }
    }

    private fun drawField(fieldModel: FieldModel): FieldView {
        val context = requireContext()
        return when (fieldModel.valueType) {
            Constants.FieldTypes.DOUBLE -> FieldViewDouble(context, fieldModel, this)
            Constants.FieldTypes.TEXT -> FieldViewText(context, fieldModel, this)
            Constants.FieldTypes.TAG -> FieldViewSpinner(context, fieldModel, mTagList ?: emptyList(), this)
            Constants.FieldTypes.DATE -> FieldViewDate(context, fieldModel, this)
            Constants.FieldTypes.LONG -> FieldViewLong(context, fieldModel, this)
            Constants.FieldTypes.BOOL -> FieldViewSpinner(context, fieldModel, mTagList ?: emptyList(), this)
            else -> FieldViewText(context, fieldModel, this)
        }
    }

    override fun onAddPhotoClick() {
        if (checkCameraPermission()) {
            easyImage?.openCameraForImage(this)
        } else {
            requestCameraPermission()
        }
    }

    private fun checkCameraPermission() = ContextCompat.checkSelfPermission(requireContext(), "android.permission.CAMERA") == 0

    private fun requestCameraPermission() {
        requestPermissions(arrayOf("android.permission.CAMERA"), REQUEST_CAMERA_PERMISSION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
         val requireActivity = requireActivity()
        Intrinsics.checkExpressionValueIsNotNull(requireActivity, "requireActivity()")
        easyImage?.handleActivityResult(
            requestCode, resultCode, data, requireActivity, object :EasyImage.Callbacks{
                override fun onCanceled(source: MediaSource) {
                    Log.d("easyImage", "onCanceled $source")
                }

                override fun onImagePickerError(error: Throwable, source: MediaSource) {
                    Log.d("easyImage", "onImagePickerError $error")
                }

                override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                    Log.d("easyImage", "onMediaFilesPicked $imageFiles")
                    val markerPhotoView = requireView().findViewById<MarkerPhotoView>(R.id.view_marker_photo)
                    markerPhotoView.addImage(imageFiles.first())
                }

            }
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults.firstOrNull() != 0) {
            Snackbar.make(
                rootView?.findViewById<Toolbar>(R.id.toolbar) ?: return,
                getString(R.string.permission_camera_needed),
                BaseTransientBottomBar.LENGTH_INDEFINITE
            ).show()
        }
    }

    private fun constructNewMarkerModel(): MarkerModel {
        val markerModel = mMarkerModel ?: throw UninitializedPropertyAccessException("mMarkerModel")
        val projectModel = mProjectModel ?: throw UninitializedPropertyAccessException("mProjectModel")

        val markerModelCopy = markerModel.copy(
            projectIds = arrayListOf(projectModel.id),
            markerModel = (rootView?.findViewById<MarkerModelView>(R.id.view_marker_model) as MarkerModelView).markerModel,
            markerType = (rootView?.findViewById<MarkerTypeView>(R.id.view_marker_type) as MarkerTypeView).markerType,
            templateId = (rootView?.findViewById<MarkerTypeView>(R.id.view_marker_type) as MarkerTypeView).templateId,
            generalId = (rootView?.findViewById<MarkerIdentifierView>(R.id.view_marker_identifier) as MarkerIdentifierView).value,
            depth = (rootView?.findViewById<MarkerDepthView>(R.id.view_marker_depth) as MarkerDepthView).getValue(),
            idLocal = markerModel.idLocal ?: UUID.randomUUID().toString(),
            fields = mFieldViewList.map { it.getFieldModel() }
        )
        return markerModelCopy
    }

    override fun onMarkerTypeChange(templateModel: TemplateModel) {
        drawFieldList(templateModel.fields)
        mMarkerModel?.fields?.let {
            fillFields(it)
        }
    }

    private fun checkMainHasChanges(): Boolean {
        val newMarkerModel = (rootView?.findViewById<MarkerModelView>(R.id.view_marker_model) as MarkerModelView).markerModel
        val currentMarkerModel = mMarkerModel?.markerModel
        val newDepth = (rootView?.findViewById<MarkerDepthView>(R.id.view_marker_depth) as MarkerDepthView).getValue()
        val currentDepth = mMarkerModel?.depth
        return newMarkerModel != currentMarkerModel || newDepth != currentDepth
    }

    private fun checkFieldsHasChanges(): Boolean {
        return mFieldViewList.any { fieldView ->
            mMarkerModel?.fields?.find { it.id == fieldView.getFieldModel().id }?.toString() != fieldView.getFieldModel().toString()
        }
    }

    private fun checkAllRequiredFilled(): Boolean {
        return (rootView?.findViewById<MarkerDepthView>(R.id.view_marker_depth) as MarkerDepthView).hasValue() &&
                mFieldViewList.all { !it.isRequired() || it.hasValue() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        if (mMarkerModel?.status != null) {
            inflater.inflate(R.menu.marker_menu, menu)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete_local -> {
                viewModel.deleteLocal(mMarkerModel!!)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        fun newInstance(projectModel: ProjectModel, markerModel: MarkerModel, onMarkerChangeListener: OnMarkerChangeListener?): MarkerFragment {
            return MarkerFragment().apply {
                arguments = bundleOf(
                    "projectModel" to projectModel,
                    "marker" to markerModel
                )
                mOnMarkerChangeListener = onMarkerChangeListener
            }
        }
    }

    override fun onFieldValueChange() {
        (rootView?.findViewById<MaterialButton>(R.id.btn_save) as MaterialButton).isEnabled =
            checkMainHasChanges() || checkFieldsHasChanges()
    }

    override fun onPhotoClick(imageDataModel: ImageDataModel) {
        getChildFragmentClickListener().onFragmentAdd(
            PhotoViewFragment.newInstance(imageDataModel)
        )
    }
}