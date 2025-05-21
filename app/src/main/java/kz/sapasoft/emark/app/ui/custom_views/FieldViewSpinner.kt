package kz.sapasoft.emark.app.ui.custom_views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import com.example.decompiledapk.R
import com.google.android.material.textview.MaterialTextView
import kz.sapasoft.emark.app.domain.model.FieldModel
import kz.sapasoft.emark.app.domain.model.TagModel
import kz.sapasoft.emark.app.ui.marker.OnFieldValueChangeListener

class FieldViewSpinner @JvmOverloads constructor(
    context: Context,
    private var mField: FieldModel,
    private val mTagList: List<TagModel>,
    listener: OnFieldValueChangeListener,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FieldView(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_field_spinner, this)
        findViewById<MaterialTextView>(R.id.tv_title).text = mField.name

        val matchingTags = mTagList.filter { it.templateId == mField.tagTemplateId }
        val tagNames = matchingTags.map { it.name }.toMutableList()

        val spinner = findViewById<Spinner>(R.id.spinner).apply {
            adapter = ArrayAdapter(context, R.layout.item_spinner, tagNames)
            setSelection(matchingTags.indexOfFirst { it.templateId == mField.tagTemplateId })
            setBackgroundResource(R.drawable.spinner_background)
            onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: android.widget.AdapterView<*>, view: View, position: Int, id: Long) {
                    listener.onFieldValueChange()
                }

                override fun onNothingSelected(parent: android.widget.AdapterView<*>) {}
            }
        }
    }

    override fun getFieldModel(): FieldModel {
        val spinner = findViewById<Spinner>(R.id.spinner)
        if (spinner.selectedItemPosition != -1) {
            mField.tagValue = mTagList[spinner.selectedItemPosition]
        }
        return mField
    }

    override fun setFieldModel(field: FieldModel) {
        mField = field.copy()
        val matchingTag = mTagList.find { it.id == field.tagValue?.id }
        val spinner = findViewById<Spinner>(R.id.spinner)
        spinner.setSelection(mTagList.indexOf(matchingTag))
    }

    override fun isRequired(): Boolean {
        return mField.required ?: false
    }

    override fun hasValue(): Boolean {
        return true
    }
}