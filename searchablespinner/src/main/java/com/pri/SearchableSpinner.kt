package com.pri

/**
 * Created by Priyanka on 2019-09-16.
 */
import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.text.InputType
import android.util.AttributeSet
import android.view.*
import android.view.accessibility.AccessibilityEvent
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import androidx.customview.view.AbsSavedState
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.pri.searchablespinnerlibrary.R
import java.io.Serializable
import java.util.*

/**
 * Layout which wraps an [TextInputEditText] to show a floating label when the hint is hidden due to
 * the user inputting text.
 *
 * @see [TextInputLayout]
 * @author Tiago Pereira (tiagomiguelmoreirapereira@gmail.com)
 */
open class SearchableSpinner @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : TextInputLayout(context, attrs), View.OnTouchListener, OnItemClickListener {
    companion object {
        /**
         * Represents an invalid position.
         * All valid positions are in the range 0 to 1 less than the number of items in the current
         * adapter.
         */
        const val INVALID_POSITION = -1
    }

    private val colorStateList: ColorStateList

    /**
     * The view that will display the available list of choices.
     */
    private val popup: BottomSheetPopup

    /**
     * The view that will display the selected item.
     */
    private val editText = TextInputEditText(getContext())

    /**
     * Extended [android.widget.Adapter] that is the bridge between this Spinner and its data.
     */

    var data: List<Any> = ArrayList()
        set(value) {
            field = value
            selection = INVALID_POSITION
        }

    /**
     * The listener that receives notifications when an item is selected.
     */
    var onItemSelectedListener: OnItemSelectedListener? = null

    /**
     * The listener that receives notifications when an item is clicked.
     */
    var onItemClickListener: OnItemClickListener? = null

    /**
     * The layout direction of this view.
     * {@link #LAYOUT_DIRECTION_RTL} if the layout direction is RTL.
     * {@link #LAYOUT_DIRECTION_LTR} if the layout direction is not RTL.
     */
    private var direction =
            if (isLayoutRtl()) ViewCompat.LAYOUT_DIRECTION_RTL else ViewCompat.LAYOUT_DIRECTION_LTR

    /**
     * The currently selected item.
     */
    var selection = INVALID_POSITION
        set(value) {
            if (field != value) {
                field = value
                data.apply {
                    val itemCount = data.count()
                    if (value in 0 until itemCount) {
                        val item = data[value]
                        editText.setText(
                                when (item) {
                                    is CharSequence -> item
                                    else -> item.toString()
                                }
                        )
                        onItemSelectedListener?.onItemSelected(item = item, position = value)
                        error = null
                    } else {
                        editText.setText(prompt)
                        editText.clearFocus()
                    }
                }
            }
        }
    /**
     * Sets the [prompt] to display when the dialog is shown.
     *
     * @return The prompt to display when the dialog is shown.
     */
    var prompt: CharSequence?
        set(value) {
            popup.setPromptText(value)
        }
        get() = popup.getPrompt()

    /**
     * @return The data corresponding to the currently selected item, or null if there is nothing
     * selected.
     */
    var selectedItem: Any?
        get() = data.getOrNull(selection)
        set(value) {
            selection = data.indexOf(value)
        }

    init {
        context.obtainStyledAttributes(attrs, R.styleable.MaterialSpinner).run {
            getInt(R.styleable.MaterialSpinner_android_gravity, -1).let {
                if (it > -1) {
                    gravity = it
                    editText.gravity = it
                } else {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        @SuppressLint("RtlHardcoded")
                        if (isLayoutRtl()) {
                            gravity = Gravity.RIGHT
                            editText.gravity = Gravity.RIGHT
                        } else {
                            gravity = Gravity.LEFT
                            editText.gravity = Gravity.LEFT
                        }
                    }
                }
            }
            editText.isEnabled =
                    getBoolean(R.styleable.MaterialSpinner_android_enabled, editText.isEnabled)
            editText.isFocusable =
                    getBoolean(R.styleable.MaterialSpinner_android_focusable, editText.isFocusable)
            editText.isFocusableInTouchMode = getBoolean(
                    R.styleable.MaterialSpinner_android_focusableInTouchMode,
                    editText.isFocusableInTouchMode
            )
            getColorStateList(R.styleable.MaterialSpinner_android_textColor)?.let {
                editText.setTextColor(
                        it
                )
            }
            getDimensionPixelSize(
                    R.styleable.MaterialSpinner_android_textSize,
                    -1
            ).let { if (it > 0) editText.textSize = it.toFloat() }
            getText(R.styleable.MaterialSpinner_android_text)?.let {
                // Allow text in debug mode for preview purposes.
                if (isInEditMode) {
                    editText.setText(it)
                } else {
                    throw RuntimeException(
                            "Don't set text directly." +
                                    "You probably want setSelection instead."
                    )
                }
            }
            popup = BottomSheetPopup(context, getString(R.styleable.MaterialSpinner_android_prompt))

            // Create the color state list.
            //noinspection Recycle
            colorStateList = context.obtainStyledAttributes(
                    attrs,
                    intArrayOf(R.attr.colorControlActivated, R.attr.colorControlNormal)
            ).run {
                val activated = getColor(0, 0)
                @SuppressLint("ResourceType")
                val normal = getColor(1, 0)
                recycle()
                ColorStateList(
                        arrayOf(
                                intArrayOf(android.R.attr.state_pressed),
                                intArrayOf(android.R.attr.state_focused),
                                intArrayOf()
                        ), intArrayOf(activated, activated, normal)
                )
            }
            // Set the arrow and properly tint it.
            getContext().getDrawableCompat(
                    getResourceId(
                            R.styleable.MaterialSpinner_android_src,
                            getResourceId(
                                    R.styleable.MaterialSpinner_srcCompat,
                                    R.drawable.ic_spinner_drawable
                            )
                    ), getContext().theme
            ).let {
                setDrawable(it)
            }

            recycle()
        }

        this.addView(editText, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))

        popup.setOnDismissListener(object : OnDismissListener {
            override fun onDismiss() {
                editText.clearFocus()
            }
        })

        // Disable input.
        editText.maxLines = 1
        editText.inputType = InputType.TYPE_NULL

        /* editText.setOnClickListener {
             popup.show(selection)
         }

         editText.onFocusChangeListener.let {
             editText.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
                 v.handler.post {
                     if (hasFocus) {
                         v.performClick()
                     }
                     it?.onFocusChange(v, hasFocus)
                 }
             }
         }*/
        editText.setOnTouchListener(this)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            if (data.isNullOrEmpty()) {
                Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show()
            } else {
                popup.show()
            }
        }
        return true
    }

    private fun setDrawable(drawable: Drawable?, applyTint: Boolean = true) {
        val delta = (paddingBottom - paddingTop) / 2
        drawable?.let { DrawableCompat.wrap(drawable) }?.apply {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            if (applyTint) {
                DrawableCompat.setTintList(this, colorStateList)
                DrawableCompat.setTintMode(this, PorterDuff.Mode.SRC_IN)
            }
        }.let {
            endIconDrawable = it
            endIconMode = END_ICON_CUSTOM
            /* startIconDrawable?.apply {
                 setBounds(0, 0, intrinsicWidth, intrinsicHeight)
             }
             if (isLayoutRtl()) {
                 Pair(it, startIconDrawable)
             } else {
                 Pair(startIconDrawable, it)
             }*/
        }/*.let { (left, right) ->
            editText.setCompoundDrawables(left, null, right, null)
            editText.compoundDrawables.forEach {
                it?.run {
                    bounds = copyBounds().apply {
                        top += delta
                        bottom += delta
                    }
                }
            }
        }*/
    }

    override fun setOnClickListener(l: OnClickListener?) {
        throw RuntimeException(
                "Don't call setOnClickListener." +
                        "You probably want setOnItemClickListener instead."
        )
    }

    /**
     * Set whether this view can receive the focus.
     * Setting this to false will also ensure that this view is not focusable in touch mode.
     *
     * @param [focusable] If true, this view can receive the focus.
     *
     * @see [android.view.View.setFocusableInTouchMode]
     * @see [android.view.View.setFocusable]
     * @attr ref android.R.styleable#View_focusable
     */
    override fun setFocusable(focusable: Boolean) {
        editText.isFocusable = focusable
        super.setFocusable(focusable)
    }

    /**
     * Set whether this view can receive focus while in touch mode.
     * Setting this to true will also ensure that this view is focusable.
     *
     * @param [focusableInTouchMode] If true, this view can receive the focus while in touch mode.
     *
     * @see [android.view.View.setFocusable]
     * @attr ref android.R.styleable#View_focusableInTouchMode
     */
    override fun setFocusableInTouchMode(focusableInTouchMode: Boolean) {
        editText.isFocusableInTouchMode = focusableInTouchMode
        super.setFocusableInTouchMode(focusableInTouchMode)
    }

    /**
     * @see [android.view.View.onRtlPropertiesChanged]
     */
    override fun onRtlPropertiesChanged(layoutDirection: Int) {
        if (direction != layoutDirection) {
            direction = layoutDirection
            editText.compoundDrawables.let {
                editText.setCompoundDrawablesWithIntrinsicBounds(it[2], null, it[0], null)
            }
        }
        super.onRtlPropertiesChanged(layoutDirection)
    }

    override fun setError(errorText: CharSequence?) {
        super.setError(errorText)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            findViewById<TextView>(R.id.textinput_error)?.let { errorView ->
                errorView.gravity = editText.gravity
                when (val p = errorView.parent) {
                    is View -> {
                        p.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                    }
                }
            }
        }
    }

    /**
     * Call the OnItemClickListener, if it is defined.
     * Performs all normal actions associated with clicking: reporting accessibility event, playing
     * a sound, etc.
     *
     * @param [view] The view within the adapter that was clicked.
     * @param [position] The position of the view in the adapter.
     * @param [id] The row id of the item that was clicked.
     * @return True if there was an assigned OnItemClickListener that was called, false otherwise is
     * returned.
     */
    fun performItemClick(view: View?, position: Int, id: Long): Boolean {
        return run {
            onItemClickListener?.let {
                playSoundEffect(SoundEffectConstants.CLICK)
                it.onItemClick(this@SearchableSpinner, view, position, id)
                true
            } ?: false
        }.also {
            view?.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_CLICKED)
        }
    }

    /**
     * Sets the prompt to display when the dialog is shown.
     *
     * @param [promptId] the resource ID of the prompt to display when the dialog is shown.
     */
    fun setPromptId(promptId: Int) {
        prompt = context.getText(promptId)
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onSaveInstanceState(): Parcelable? {
        return super.onSaveInstanceState()?.let {
            SavedState(it).apply {
                this.selection = this@SearchableSpinner.selection
                this.isShowingPopup = this@SearchableSpinner.popup.isShowing()
            }
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        when (state) {
            is SavedState -> {
                super.onRestoreInstanceState(state.superState)
                selection = state.selection
                if (state.isShowingPopup) {
                    viewTreeObserver?.addOnGlobalLayoutListener(object :
                            ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            if (!popup.isShowing()) {
                                requestFocus()
                            }
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                                viewTreeObserver?.removeOnGlobalLayoutListener(this)
                            } else {
                                viewTreeObserver?.removeGlobalOnLayoutListener(this)
                            }
                        }
                    })
                }
            }
            else -> super.onRestoreInstanceState(state)
        }
    }

    /**
     * Returns if this view layout should be in a RTL direction.
     * @return True if is RTL, false otherwise .
     */
    private fun isLayoutRtl(): Boolean {
        return Locale.getDefault().isLayoutRtl()
    }

    /**
     * Returns if this Locale direction is RTL.
     * @return True if is RTL, false otherwise .
     */
    private fun Locale.isLayoutRtl(): Boolean {
        return TextUtilsCompat.getLayoutDirectionFromLocale(this) == ViewCompat.LAYOUT_DIRECTION_RTL
    }

    /**
     * @see [android.support.v4.content.res.ResourcesCompat.getDrawable]
     */
    private fun Context.getDrawableCompat(
            @DrawableRes id: Int,
            theme: Resources.Theme?
    ): Drawable? {
        return resources.getDrawableCompat(id, theme)
    }

    /**
     * @see [android.support.v4.content.res.ResourcesCompat.getDrawable]
     */
    @Throws(Resources.NotFoundException::class)
    private fun Resources.getDrawableCompat(
            @DrawableRes id: Int,
            theme: Resources.Theme?
    ): Drawable? {
        return ResourcesCompat.getDrawable(this, id, theme)?.let { DrawableCompat.wrap(it) }
    }

    var listAdapter: SearchListAdapter? = null

    private inner class BottomSheetPopup(
            val context: Context,
            private var prompt: CharSequence? = null
    ) {
        private val popup by lazy {
            SearchableListDialog.newInstance().apply {
                setOnSearchableItemClickListener(this@SearchableSpinner)
                setTitle(hint.toString())
                listAdapter?.let { adapter = listAdapter }
            }
        }
        private var listener: OnDismissListener? = null


        fun setPromptText(hintText: CharSequence?) {
            prompt = hintText
        }

        fun getPrompt(): CharSequence? {
            return prompt
        }

        fun show() {
            scanForActivity(context)?.supportFragmentManager?.let { it1 ->
                popup.show(it1, "TAG").apply {
                    popup.setData(data)
                }
            }


        }

        fun setOnDismissListener(listener: OnDismissListener?) {
            this.listener = listener
        }

        fun dismiss() {
            popup.dismiss()
            listener?.onDismiss()
        }

        fun isShowing() = !popup.isHidden
    }

    /**
     * Listener that is called when this popup window is dismissed.
     */
    interface OnDismissListener {
        /**
         * Called when this popup window is dismissed.
         */
        fun onDismiss()
    }

    private fun scanForActivity(cont: Context?): AppCompatActivity? {
        if (cont == null)
            return null
        else if (cont is AppCompatActivity)
            return cont
        else if (cont is ContextWrapper)
            return scanForActivity(cont.baseContext)

        return null
    }


    /**
     * Interface for a callback to be invoked when an item in this view has been selected.
     */
    interface OnItemSelectedListener {
        fun onItemSelected(item: Any, position: Int)
    }


    internal class SavedState : AbsSavedState {
        var selection: Int = INVALID_POSITION
        var isShowingPopup: Boolean = false

        constructor(superState: Parcelable) : super(superState)

        constructor(source: Parcel, loader: ClassLoader?) : super(source, loader) {
            selection = source.readInt()
            isShowingPopup = source.readByte().toInt() != 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeInt(selection)
            dest.writeByte((if (isShowingPopup) 1 else 0).toByte())
        }

        override fun toString(): String {
            return ("SearchableSpinner.SavedState{" +
                    Integer.toHexString(System.identityHashCode(this)) +
                    " selection=" +
                    selection +
                    ", isShowingPopup=" +
                    isShowingPopup +
                    "}")
        }

        companion object CREATOR : Parcelable.ClassLoaderCreator<SavedState> {

            override fun createFromParcel(parcel: Parcel): SavedState {
                return SavedState(parcel, null)
            }

            override fun createFromParcel(parcel: Parcel, loader: ClassLoader): SavedState {
                return SavedState(parcel, loader)
            }

            override fun newArray(size: Int): Array<SavedState?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun onItemClick(parent: SearchableSpinner?, view: View?, position: Int, id: Long) {
        this@SearchableSpinner.selection = position
        onItemClickListener?.let {
            this@SearchableSpinner.performItemClick(
                    view,
                    position,
                    id
            )
        }
        popup.dismiss()
    }

}

/**
 * Interface definition for a callback to be invoked when an item in this View has been clicked.
 */
interface OnItemClickListener : Serializable {

    /**
     * Callback method to be invoked when an item in this View has been clicked.
     * Implementers can call getItemAtPosition(position) if they need to access the data
     * associated with the selected item.
     *
     * @param [parent] The View where the click happened.
     * @param [view] The view within the adapter that was clicked (this will be a view provided
     * by the adapter).
     * @param [position] The position of the view in the adapter.
     * @param [id] The row id of the item that was clicked.
     */
    fun onItemClick(parent: SearchableSpinner? = null, view: View?, position: Int, id: Long)
}