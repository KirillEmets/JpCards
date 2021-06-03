package com.kirillemets.flashcards.preference

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.TypedArray
import android.os.Bundle
import android.util.AttributeSet
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.preference.Preference
import com.google.android.material.slider.Slider
import com.kirillemets.flashcards.R

class MaterialSliderPreference(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
    Preference(context, attrs, defStyleAttr, defStyleRes), Slider.OnChangeListener {
    private val sliderDialogFragment = SliderDialogFragment(this) {
        if(summaryProvider == null)
            summary = currentValue.toString()
    }
    private var fragmentManager: FragmentManager? = null

    private var min: Float = 0f
    private var max: Float = 1f
    private var step: Float = 0.01f
    private var currentValue: Float
        get() = getPersistedFloat(min)
        set(value) {
            persistFloat(value)
            notifyChanged()
        }

    private var defaultValue = 0f

    override fun onGetDefaultValue(ta: TypedArray?, i: Int): Any {
        defaultValue = ta!!.getFloat(i, 0F)
        return defaultValue
    }

    override fun onSetInitialValue(initValue: Any?) {
        currentValue = getPersistedFloat(
            if (initValue is Float) initValue
            else this.defaultValue
        )
    }

    override fun onAttached() {
        summary = currentValue.toString()
        super.onAttached()
    }

    fun provideFragmentManager(fragmentManager: FragmentManager) {
        this.fragmentManager = fragmentManager
    }

    override fun onClick() {
        sliderDialogFragment.setValues(title.toString(), currentValue, min, max, step)
        fragmentManager?.let {
            sliderDialogFragment.show(it, "MaterialSliderDialog")
        }
    }

    fun setBounds(valueFrom: Float, valueTo: Float, step: Float) {
        if(valueTo <= valueFrom)
            throw IllegalStateException("parameter valueFrom(${valueFrom}) must be less than valueTo(${valueTo})")

        min = valueFrom
        max = valueTo
        this.step = step
        currentValue = currentValue.coerceIn(min, max)
    }

    override fun onValueChange(slider: Slider, value: Float, fromUser: Boolean) {
        currentValue = value
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.attr.preferenceStyle)
    constructor(context: Context) : this(context, null)
}

class SliderDialogFragment(private val listener: Slider.OnChangeListener, val onClose: () -> Unit): DialogFragment() {
    var dialogTitle: String = ""
    var currentValue: Float = 0f
    var max: Float = 0f
    var min: Float = 0f
    var step: Float = 0f

    fun setValues(title: String, value: Float, min: Float, max: Float, step: Float) {
        this.currentValue = value
        this.min = min
        this.max = max
        this.step = step
        dialogTitle = title
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { act ->
            val builder = AlertDialog.Builder(act)
            val inflater = act.layoutInflater
            val root = inflater.inflate(R.layout.fragment_material_slider_dialog, null)
            root.findViewById<TextView>(R.id.title).text = dialogTitle
            root.findViewById<Slider>(R.id.material_slider).apply {
                value = currentValue
                valueFrom = min
                valueTo = max
                stepSize = step
                addOnChangeListener(listener)
            }
            builder.setView(root)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onDismiss(dialog: DialogInterface) {
        onClose()
        super.onDismiss(dialog)
    }
}