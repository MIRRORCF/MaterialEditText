package com.mirrorcf.materialedittext

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.*
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import androidx.appcompat.widget.AppCompatEditText
import com.mirrorcf.materialedittext.Colors.isLight
import com.mirrorcf.materialedittext.Density.dp2px
import com.mirrorcf.materialedittext.validation.METLengthChecker
import com.mirrorcf.materialedittext.validation.METValidator
import com.nineoldandroids.animation.ArgbEvaluator
import com.nineoldandroids.animation.ObjectAnimator
import java.util.*
import java.util.regex.Pattern


class MaterialEditText : AppCompatEditText {
    @IntDef(FLOATING_LABEL_NONE, FLOATING_LABEL_NORMAL, FLOATING_LABEL_HIGHLIGHT)
    annotation class FloatingLabelType

    val tag = "material"

    /**
     * the spacing between the main text and the inner top padding.
     */
    private var extraPaddingTop = 0

    /**
     * the spacing between the main text and the inner bottom padding.
     */
    private var extraPaddingBottom = 0

    /**
     * the extra spacing between the main text and the left, actually for the left icon.
     */
    private var extraPaddingLeft = 0

    /**
     * the extra spacing between the main text and the right, actually for the right icon.
     */
    private var extraPaddingRight = 0

    /**
     * the floating label's text size.
     */
    private var floatingLabelTextSize = 0

    /**
     * the floating label's text color.
     */
    private var floatingLabelTextColor = 0

    /**
     * the bottom texts' size.
     */
    private var bottomTextSize = 0

    /**
     * the spacing between the main text and the floating label.
     */
    private var floatingLabelPadding = 0

    /**
     * the spacing between the main text and the bottom components (bottom ellipsis, helper/error text, characters counter).
     */
    private var bottomSpacing = 0

    /**
     * whether the floating label should be shown. default is false.
     */
    private var floatingLabelEnabled = false

    /**
     * whether to highlight the floating label's text color when focused (with the main color). default is true.
     */
    private var highlightFloatingLabel = false

    /**
     * the base color of the line and the texts. default is black.
     */
    private var baseColor = 0
    /**
     * get inner top padding, not the real paddingTop
     */
    /**
     * inner top padding
     */
    var innerPaddingTop = 0
        private set
    /**
     * get inner bottom padding, not the real paddingBottom
     */
    /**
     * inner bottom padding
     */
    var innerPaddingBottom = 0
        private set
    /**
     * get inner left padding, not the real paddingLeft
     */
    /**
     * inner left padding
     */
    var innerPaddingLeft = 0
        private set
    /**
     * get inner right padding, not the real paddingRight
     */
    /**
     * inner right padding
     */
    var innerPaddingRight = 0
        private set

    /**
     * the underline's highlight color, and the highlight color of the floating label if app:highlightFloatingLabel is set true in the xml. default is black(when app:darkTheme is false) or white(when app:darkTheme is true)
     */
    private var primaryColor = 0

    /**
     * the color for when something is wrong.(e.g. exceeding max characters)
     */
    private var errorColor = 0

    /**
     * min characters count limit. 0 means no limit. default is 0. NOTE: the character counter will increase the View's height.
     */
    private var minCharacters = 0

    /**
     * max characters count limit. 0 means no limit. default is 0. NOTE: the character counter will increase the View's height.
     */
    private var maxCharacters = 0

    /**
     * whether to show the bottom ellipsis in singleLine mode. default is false. NOTE: the bottom ellipsis will increase the View's height.
     */
    private var singleLineEllipsis = false

    /**
     * Always show the floating label, instead of animating it in/out. False by default.
     */
    private var floatingLabelAlwaysShown = false

    /**
     * Always show the helper text, no matter if the edit text is focused. False by default.
     */
    private var helperTextAlwaysShown = false

    /**
     * bottom ellipsis's height
     */
    private var bottomEllipsisSize = 0

    /**
     * min bottom lines count.
     */
    private var minBottomLines = 0

    /**
     * reserved bottom text lines count, no matter if there is some helper/error text.
     */
    private var minBottomTextLines = 0

    /**
     * real-time bottom lines count. used for bottom extending/collapsing animation.
     */
    private var currentBottomLines = 0f

    /**
     * bottom lines count.
     */
    private var bottomLines = 0f

    /**
     * Helper text at the bottom
     */
    private var helperText: String? = null

    /**
     * Helper text color
     */
    private var helperTextColor = -1

    /**
     * error text for manually invoked [.setError]
     */
    private var tempErrorText: String? = null

    /**
     * animation fraction of the floating label (0 as totally hidden).
     */
    private var floatingLabelFraction = 0f

    /**
     * whether the floating label is being shown.
     */
    private var floatingLabelShown = false

    /**
     * the floating label's focusFraction
     */
    private var focusFraction = 0f

    /**
     * The font used for the accent texts (floating label, error/helper text, character counter, etc.)
     */
    private var accentTypeface: Typeface? = null

    /**
     * The font used on the view (EditText content)
     */
    private var customTypeface: Typeface? = null

    /**
     * Text for the floatLabel if different from the hint
     */
    private var floatingLabelText: CharSequence? = null

    /**
     * Whether or not to show the underline. Shown by default
     */
    private var hideUnderline = false

    /**
     * Underline's color
     */
    private var underlineColor = 0

    /**
     * Whether to validate as soon as the text has changed. False by default
     */
    private var autoValidate = false

    /**
     * Whether the characters count is valid
     */
    var isCharactersCountValid = false
        private set

    /**
     * Whether use animation to show/hide the floating label.
     */
    var isFloatingLabelAnimating = false

    /**
     * Whether check the characters count at the beginning it's shown.
     */
    private var checkCharactersCountAtBeginning = false

    /**
     * Left Icon
     */
    private var iconLeftBitmaps: Array<Bitmap>? = null

    /**
     * Right Icon
     */
    private var iconRightBitmaps: Array<Bitmap>? = null

    /**
     * Clear Button
     */
    private var clearButtonBitmaps: Array<Bitmap>? = null

    /**
     * Secret Button ON
     */
    private var secretOnButtonBitmaps: Array<Bitmap>? = null

    /**
     * Secret Button OFF
     */
    private var secretOffButtonBitmaps: Array<Bitmap>? = null

    /**
     * Auto validate when focus lost.
     */
    var isValidateOnFocusLost = false
    private var showClearButton = false
    private var showSecretButton = false
    private var secretVisibility = false
    private var firstShown = false
    private var iconSize = 0
    private var iconOuterWidth = 0
    private var iconOuterHeight = 0
    private var iconPadding = 0
    private var clearButtonTouched = false
    private var clearButtonClicking = false
    private var secretButtonTouched = false
    private var secretButtonClicking = false
    private var textColorStateList: ColorStateList? = null
    private var textColorHintStateList: ColorStateList? = null
    private val focusEvaluator = ArgbEvaluator()
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private var textLayout: StaticLayout? = null
    private var labelAnimator: ObjectAnimator? = null
    private var labelFocusAnimator: ObjectAnimator? = null
    private var bottomLinesAnimator: ObjectAnimator? = null
    private var innerFocusChangeListener: OnFocusChangeListener? = null
    private var outerFocusChangeListener: OnFocusChangeListener? = null
    private var validators: MutableList<METValidator>? = null
    private var lengthChecker: METLengthChecker? = null

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, style: Int) : super(context, attrs, style) {
        init(context, attrs)
    }

    @SuppressLint("ResourceType")
    private fun init(context: Context, attrs: AttributeSet?) {
        if (isInEditMode) {
            return
        }
        iconSize = getPixel(32)
        iconOuterWidth = getPixel(48)
        iconOuterHeight = getPixel(32)
        bottomSpacing = resources.getDimensionPixelSize(R.dimen.inner_components_spacing)
        bottomEllipsisSize = resources.getDimensionPixelSize(R.dimen.bottom_ellipsis_height)

        // default baseColor is black
        val defaultBaseColor = Color.BLACK
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaterialEditText)
        textColorStateList = typedArray.getColorStateList(R.styleable.MaterialEditText_met_textColor)
        textColorHintStateList = typedArray.getColorStateList(R.styleable.MaterialEditText_met_textColorHint)
        baseColor = typedArray.getColor(R.styleable.MaterialEditText_met_baseColor, defaultBaseColor)

        // retrieve the default primaryColor
        val defaultPrimaryColor: Int
        val primaryColorTypedValue = TypedValue()
        defaultPrimaryColor = try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                context.theme.resolveAttribute(android.R.attr.colorPrimary, primaryColorTypedValue, true)
                primaryColorTypedValue.data
            } else {
                throw RuntimeException("SDK_INT less than LOLLIPOP")
            }
        } catch (e: Exception) {
            try {
                val colorPrimaryId = resources.getIdentifier("colorPrimary", "attr", getContext().packageName)
                if (colorPrimaryId != 0) {
                    context.theme.resolveAttribute(colorPrimaryId, primaryColorTypedValue, true)
                    primaryColorTypedValue.data
                } else {
                    throw RuntimeException("colorPrimary not found")
                }
            } catch (e1: Exception) {
                baseColor
            }
        }
        primaryColor = typedArray.getColor(R.styleable.MaterialEditText_met_primaryColor, defaultPrimaryColor)
        setFloatingLabelInternal(typedArray.getInt(R.styleable.MaterialEditText_met_floatingLabel, 0))
        errorColor = typedArray.getColor(R.styleable.MaterialEditText_met_errorColor, Color.parseColor("#e7492E"))
        minCharacters = typedArray.getInt(R.styleable.MaterialEditText_met_minCharacters, 0)
        maxCharacters = typedArray.getInt(R.styleable.MaterialEditText_met_maxCharacters, 0)
        singleLineEllipsis = typedArray.getBoolean(R.styleable.MaterialEditText_met_singleLineEllipsis, false)
        helperText = typedArray.getString(R.styleable.MaterialEditText_met_helperText)
        helperTextColor = typedArray.getColor(R.styleable.MaterialEditText_met_helperTextColor, -1)
        minBottomTextLines = typedArray.getInt(R.styleable.MaterialEditText_met_minBottomTextLines, 0)
        val fontPathForAccent = typedArray.getString(R.styleable.MaterialEditText_met_accentTypeface)
        if (fontPathForAccent != null && !isInEditMode) {
            accentTypeface = getCustomTypeface(fontPathForAccent)
            textPaint.typeface = accentTypeface
        }
        val fontPathForView = typedArray.getString(R.styleable.MaterialEditText_met_typeface)
        if (fontPathForView != null && !isInEditMode) {
            customTypeface = getCustomTypeface(fontPathForView)
            setTypeface(customTypeface)
        }
        floatingLabelText = typedArray.getString(R.styleable.MaterialEditText_met_floatingLabelText)
        if (floatingLabelText == null) {
            floatingLabelText = hint
        }
        floatingLabelPadding = typedArray.getDimensionPixelSize(R.styleable.MaterialEditText_met_floatingLabelPadding, bottomSpacing)
        floatingLabelTextSize = typedArray.getDimensionPixelSize(R.styleable.MaterialEditText_met_floatingLabelTextSize, resources.getDimensionPixelSize(R.dimen.floating_label_text_size))
        floatingLabelTextColor = typedArray.getColor(R.styleable.MaterialEditText_met_floatingLabelTextColor, -1)
        isFloatingLabelAnimating = typedArray.getBoolean(R.styleable.MaterialEditText_met_floatingLabelAnimating, true)
        bottomTextSize = typedArray.getDimensionPixelSize(R.styleable.MaterialEditText_met_bottomTextSize, resources.getDimensionPixelSize(R.dimen.bottom_text_size))
        hideUnderline = typedArray.getBoolean(R.styleable.MaterialEditText_met_hideUnderline, false)
        underlineColor = typedArray.getColor(R.styleable.MaterialEditText_met_underlineColor, -1)
        autoValidate = typedArray.getBoolean(R.styleable.MaterialEditText_met_autoValidate, false)
        iconLeftBitmaps = generateIconBitmaps(typedArray.getResourceId(R.styleable.MaterialEditText_met_iconLeft, -1))
        iconRightBitmaps = generateIconBitmaps(typedArray.getResourceId(R.styleable.MaterialEditText_met_iconRight, -1))
        showClearButton = typedArray.getBoolean(R.styleable.MaterialEditText_met_clearButton, false)
        showSecretButton = typedArray.getBoolean(R.styleable.MaterialEditText_met_secretButton, false)
        clearButtonBitmaps = generateIconBitmaps(R.mipmap.met_icon_clear)
        secretOnButtonBitmaps = generateIconBitmaps(R.mipmap.met_icon_eye_on)
        secretOffButtonBitmaps = generateIconBitmaps(R.mipmap.met_icon_eye_off)
        iconPadding = typedArray.getDimensionPixelSize(R.styleable.MaterialEditText_met_iconPadding, getPixel(16))
        floatingLabelAlwaysShown = typedArray.getBoolean(R.styleable.MaterialEditText_met_floatingLabelAlwaysShown, false)
        helperTextAlwaysShown = typedArray.getBoolean(R.styleable.MaterialEditText_met_helperTextAlwaysShown, false)
        isValidateOnFocusLost = typedArray.getBoolean(R.styleable.MaterialEditText_met_validateOnFocusLost, false)
        checkCharactersCountAtBeginning = typedArray.getBoolean(R.styleable.MaterialEditText_met_checkCharactersCountAtBeginning, true)
        typedArray.recycle()
        val paddings = intArrayOf(
                android.R.attr.padding,  // 0
                android.R.attr.paddingLeft,  // 1
                android.R.attr.paddingTop,  // 2
                android.R.attr.paddingRight,  // 3
                android.R.attr.paddingBottom // 4
        )
        val paddingsTypedArray = context.obtainStyledAttributes(attrs, paddings)
        val padding = paddingsTypedArray.getDimensionPixelSize(0, 0)
        innerPaddingLeft = paddingsTypedArray.getDimensionPixelSize(1, padding)
        innerPaddingTop = paddingsTypedArray.getDimensionPixelSize(2, padding)
        innerPaddingRight = paddingsTypedArray.getDimensionPixelSize(3, padding)
        innerPaddingBottom = paddingsTypedArray.getDimensionPixelSize(4, padding)
        paddingsTypedArray.recycle()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            background = null
        } else {
            setBackgroundDrawable(null)
        }
        if (singleLineEllipsis) {
            val transformationMethod = transformationMethod
            setSingleLine()
            setTransformationMethod(transformationMethod)
        }
        initMinBottomLines()
        initPadding()
        initText()
        initFloatingLabel()
        initTextWatcher()
        checkCharactersCount()
    }

    private fun initText() {
        if (!TextUtils.isEmpty(text)) {
            val text: CharSequence? = text
            setText(null)
            resetHintTextColor()
            setText(text)
            setSelection(text!!.length)
            floatingLabelFraction = 1f
            floatingLabelShown = true
        } else {
            resetHintTextColor()
        }
        resetTextColor()
    }

    private fun initTextWatcher() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                checkCharactersCount()
                if (autoValidate) {
                    validate()
                } else {
                    setError("")
                }
                postInvalidate()
            }
        })
    }

    private fun getCustomTypeface(fontPath: String): Typeface {
        return Typeface.createFromAsset(context.assets, fontPath)
    }

    fun setIconLeft(@DrawableRes res: Int) {
        iconLeftBitmaps = generateIconBitmaps(res)
        initPadding()
    }

    fun setIconLeft(drawable: Drawable) {
        iconLeftBitmaps = generateIconBitmaps(drawable)
        initPadding()
    }

    fun setIconLeft(bitmap: Bitmap) {
        iconLeftBitmaps = generateIconBitmaps(bitmap)
        initPadding()
    }

    fun setIconRight(@DrawableRes res: Int) {
        iconRightBitmaps = generateIconBitmaps(res)
        initPadding()
    }

    fun setIconRight(drawable: Drawable) {
        iconRightBitmaps = generateIconBitmaps(drawable)
        initPadding()
    }

    fun setIconRight(bitmap: Bitmap) {
        iconRightBitmaps = generateIconBitmaps(bitmap)
        initPadding()
    }

    fun isShowClearButton(): Boolean {
        return showClearButton
    }

    fun setShowClearButton(show: Boolean) {
        showClearButton = show
        correctPaddings()
    }

    fun isShowSecretButton(): Boolean {
        return showSecretButton
    }

    fun setShowSecretButton(show: Boolean) {
        showSecretButton = show
        correctPaddings()
    }

    private fun generateIconBitmaps(@DrawableRes origin: Int): Array<Bitmap>? {
        if (origin == -1) {
            return null
        }
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, origin, options)
        val size = Math.max(options.outWidth, options.outHeight)
        options.inSampleSize = if (size > iconSize) size / iconSize else 1
        options.inJustDecodeBounds = false
        return generateIconBitmaps(BitmapFactory.decodeResource(resources, origin, options))
    }

    private fun generateIconBitmaps(drawable: Drawable): Array<Bitmap>? {
        if (drawable == null) return null
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return generateIconBitmaps(Bitmap.createScaledBitmap(bitmap, iconSize, iconSize, false))
    }

    private fun generateIconBitmaps(origin: Bitmap): Array<Bitmap>? {
        var origin: Bitmap? = origin
        if (origin == null) {
            return null
        }
        val iconBitmaps = arrayOfNulls<Bitmap>(4)
        origin = scaleIcon(origin)
        var canvas:Canvas? = null
        iconBitmaps[0] = origin.copy(Bitmap.Config.ARGB_8888, true)?.apply {
            canvas = Canvas(this)
            canvas?.drawColor(baseColor and 0x00ffffff or if (isLight(baseColor)) -0x1000000 else -0x76000000, PorterDuff.Mode.SRC_IN)
        }
        iconBitmaps[1] = origin.copy(Bitmap.Config.ARGB_8888, true)?.apply {
            canvas = Canvas(this)
            canvas?.drawColor(primaryColor, PorterDuff.Mode.SRC_IN)
        }
        iconBitmaps[2] = origin.copy(Bitmap.Config.ARGB_8888, true)?.apply {
            canvas = Canvas(this)
            canvas?.drawColor(baseColor and 0x00ffffff or if (isLight(baseColor)) 0x4c000000 else 0x42000000, PorterDuff.Mode.SRC_IN)
        }
        iconBitmaps[3] = origin.copy(Bitmap.Config.ARGB_8888, true)?.apply {
            canvas = Canvas(this)
            canvas?.drawColor(errorColor, PorterDuff.Mode.SRC_IN)
        }
        return iconBitmaps as Array<Bitmap>?
    }

    private fun scaleIcon(origin: Bitmap): Bitmap {
        val width = origin.width
        val height = origin.height
        val size = Math.max(width, height)
        return if (size == iconSize) {
            origin
        } else if (size > iconSize) {
            val scaledWidth: Int
            val scaledHeight: Int
            if (width > iconSize) {
                scaledWidth = iconSize
                scaledHeight = (iconSize * (height.toFloat() / width)).toInt()
            } else {
                scaledHeight = iconSize
                scaledWidth = (iconSize * (width.toFloat() / height)).toInt()
            }
            Bitmap.createScaledBitmap(origin, scaledWidth, scaledHeight, false)
        } else {
            origin
        }
    }

    fun getFloatingLabelFraction(): Float {
        return floatingLabelFraction
    }

    fun setFloatingLabelFraction(floatingLabelFraction: Float) {
        this.floatingLabelFraction = floatingLabelFraction
        invalidate()
    }

    fun getFocusFraction(): Float {
        return focusFraction
    }

    fun setFocusFraction(focusFraction: Float) {
        this.focusFraction = focusFraction
        invalidate()
    }

    fun getCurrentBottomLines(): Float {
        return currentBottomLines
    }

    fun setCurrentBottomLines(currentBottomLines: Float) {
        this.currentBottomLines = currentBottomLines
        initPadding()
    }

    fun isFloatingLabelAlwaysShown(): Boolean {
        return floatingLabelAlwaysShown
    }

    fun setFloatingLabelAlwaysShown(floatingLabelAlwaysShown: Boolean) {
        this.floatingLabelAlwaysShown = floatingLabelAlwaysShown
        invalidate()
    }

    fun isHelperTextAlwaysShown(): Boolean {
        return helperTextAlwaysShown
    }

    fun setHelperTextAlwaysShown(helperTextAlwaysShown: Boolean) {
        this.helperTextAlwaysShown = helperTextAlwaysShown
        invalidate()
    }

    fun getAccentTypeface(): Typeface? {
        return accentTypeface
    }

    /**
     * Set typeface used for the accent texts (floating label, error/helper text, character counter, etc.)
     */
    fun setAccentTypeface(accentTypeface: Typeface?) {
        this.accentTypeface = accentTypeface
        textPaint.typeface = accentTypeface
        postInvalidate()
    }

    fun isHideUnderline(): Boolean {
        return hideUnderline
    }

    /**
     * Set whether or not to hide the underline (shown by default).
     *
     *
     * The positions of text below will be adjusted accordingly (error/helper text, character counter, ellipses, etc.)
     *
     *
     * NOTE: You probably don't want to hide this if you have any subtext features of this enabled, as it can look weird to not have a dividing line between them.
     */
    fun setHideUnderline(hideUnderline: Boolean) {
        this.hideUnderline = hideUnderline
        initPadding()
        postInvalidate()
    }

    /**
     * get the color of the underline for normal state
     */
    fun getUnderlineColor(): Int {
        return underlineColor
    }

    /**
     * Set the color of the underline for normal state
     * @param color
     */
    fun setUnderlineColor(color: Int) {
        underlineColor = color
        postInvalidate()
    }

    fun getFloatingLabelText(): CharSequence? {
        return floatingLabelText
    }

    /**
     * Set the floating label text.
     *
     *
     * Pass null to force fallback to use hint's value.
     *
     * @param floatingLabelText
     */
    fun setFloatingLabelText(floatingLabelText: CharSequence?) {
        this.floatingLabelText = floatingLabelText ?: hint
        postInvalidate()
    }

    fun getFloatingLabelTextSize(): Int {
        return floatingLabelTextSize
    }

    fun setFloatingLabelTextSize(size: Int) {
        floatingLabelTextSize = size
        initPadding()
    }

    fun getFloatingLabelTextColor(): Int {
        return floatingLabelTextColor
    }

    fun setFloatingLabelTextColor(color: Int) {
        floatingLabelTextColor = color
        postInvalidate()
    }

    fun getBottomTextSize(): Int {
        return bottomTextSize
    }

    fun setBottomTextSize(size: Int) {
        bottomTextSize = size
        initPadding()
    }

    private fun getPixel(dp: Int): Int {
        return dp2px(context, dp.toFloat())
    }

    private fun initPadding() {
        extraPaddingTop = if (floatingLabelEnabled) floatingLabelTextSize + floatingLabelPadding else floatingLabelPadding
        textPaint.textSize = bottomTextSize.toFloat()
        val textMetrics = textPaint.fontMetrics
        extraPaddingBottom = ((textMetrics.descent - textMetrics.ascent) * currentBottomLines).toInt() + if (hideUnderline) bottomSpacing else bottomSpacing * 2
        extraPaddingLeft = if (iconLeftBitmaps == null) 0 else iconOuterWidth + iconPadding
        extraPaddingRight = if (iconRightBitmaps == null) 0 else iconOuterWidth + iconPadding
        correctPaddings()
    }

    /**
     * calculate [.minBottomLines]
     */
    private fun initMinBottomLines() {
        val extendBottom = minCharacters > 0 || maxCharacters > 0 || singleLineEllipsis || tempErrorText != null || helperText != null
        minBottomLines = if (minBottomTextLines > 0) minBottomTextLines else if (extendBottom) 1 else 0
        currentBottomLines = minBottomLines.toFloat()
    }

    /**
     * use [.setPaddings] instead, or the paddingTop and the paddingBottom may be set incorrectly.
     */
    @Deprecated("")
    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
    }

    /**
     * Use this method instead of [.setPadding] to automatically set the paddingTop and the paddingBottom correctly.
     */
    fun setPaddings(left: Int, top: Int, right: Int, bottom: Int) {
        innerPaddingTop = top
        innerPaddingBottom = bottom
        innerPaddingLeft = left
        innerPaddingRight = right
        correctPaddings()
    }

    /**
     * Set paddings to the correct values
     */
    private fun correctPaddings() {
        var buttonsWidthLeft = 0
        var buttonsWidthRight = 0
        val buttonsWidth = iconOuterWidth * buttonsCount
        if (isRTL) {
            buttonsWidthLeft = buttonsWidth
        } else {
            buttonsWidthRight = buttonsWidth
        }
        LogUtils.d(tag,"buttonsWidth = $buttonsWidth,iconOuterWidth = $iconOuterWidth, buttonsCount = $buttonsCount")
        super.setPadding(innerPaddingLeft + extraPaddingLeft + buttonsWidthLeft, innerPaddingTop + extraPaddingTop, innerPaddingRight + extraPaddingRight + buttonsWidthRight, innerPaddingBottom + extraPaddingBottom)
    }

    private val buttonsCount: Int = 0

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (!firstShown) {
            firstShown = true
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            adjustBottomLines()
        }
    }

    /**
     * @return True, if adjustments were made that require the view to be invalidated.
     */
    private fun adjustBottomLines(): Boolean {
        // Bail out if we have a zero width; lines will be adjusted during next layout.
        if (width == 0) {
            return false
        }
        val destBottomLines: Int
        textPaint.textSize = bottomTextSize.toFloat()
        if (tempErrorText != null || helperText != null) {
            val alignment = if (gravity and Gravity.RIGHT == Gravity.RIGHT || isRTL) Layout.Alignment.ALIGN_OPPOSITE else if (gravity and Gravity.LEFT == Gravity.LEFT) Layout.Alignment.ALIGN_NORMAL else Layout.Alignment.ALIGN_CENTER
            textLayout = StaticLayout(if (tempErrorText != null) tempErrorText else helperText, textPaint, width - bottomTextLeftOffset - bottomTextRightOffset - paddingLeft - paddingRight, alignment, 1.0f, 0.0f, true)
            destBottomLines = Math.max(textLayout!!.lineCount, minBottomTextLines)
        } else {
            destBottomLines = minBottomLines
        }
        if (bottomLines != destBottomLines.toFloat()) {
            getBottomLinesAnimator(destBottomLines.toFloat())!!.start()
        }
        bottomLines = destBottomLines.toFloat()
        return true
    }

    private fun initFloatingLabel() {
        // observe the text changing
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (floatingLabelEnabled) {
                    if (s.length == 0) {
                        if (floatingLabelShown) {
                            floatingLabelShown = false
                            getLabelAnimator()!!.reverse()
                        }
                    } else if (!floatingLabelShown) {
                        floatingLabelShown = true
                        getLabelAnimator()!!.start()
                    }
                }
            }
        })
        // observe the focus state to animate the floating label's text color appropriately
        innerFocusChangeListener = object : OnFocusChangeListener {
            override fun onFocusChange(v: View, hasFocus: Boolean) {
                if (floatingLabelEnabled && highlightFloatingLabel) {
                    if (hasFocus) {
                        getLabelFocusAnimator()!!.start()
                    } else {
                        getLabelFocusAnimator()!!.reverse()
                    }
                }
                if (isValidateOnFocusLost && !hasFocus) {
                    validate()
                }
                if (outerFocusChangeListener != null) {
                    outerFocusChangeListener!!.onFocusChange(v, hasFocus)
                }
            }
        }
        super.setOnFocusChangeListener(innerFocusChangeListener)
    }

    fun setBaseColor(color: Int) {
        if (baseColor != color) {
            baseColor = color
        }
        initText()
        postInvalidate()
    }

    fun setPrimaryColor(color: Int) {
        primaryColor = color
        postInvalidate()
    }

    /**
     * Same function as [.setTextColor]. (Directly overriding the built-in one could cause some error, so use this method instead.)
     */
    fun setMetTextColor(color: Int) {
        textColorStateList = ColorStateList.valueOf(color)
        resetTextColor()
    }

    /**
     * Same function as [.setTextColor]. (Directly overriding the built-in one could cause some error, so use this method instead.)
     */
    fun setMetTextColor(colors: ColorStateList?) {
        textColorStateList = colors
        resetTextColor()
    }

    private fun resetTextColor() {
        if (textColorStateList == null) {
            textColorStateList = ColorStateList(arrayOf(intArrayOf(android.R.attr.state_enabled), EMPTY_STATE_SET), intArrayOf(baseColor and 0x00ffffff or -0x21000000, baseColor and 0x00ffffff or 0x44000000))
            setTextColor(textColorStateList)
        } else {
            setTextColor(textColorStateList)
        }
    }

    /**
     * Same function as [.setHintTextColor]. (The built-in one is a final method that can't be overridden, so use this method instead.)
     */
    fun setMetHintTextColor(color: Int) {
        textColorHintStateList = ColorStateList.valueOf(color)
        resetHintTextColor()
    }

    /**
     * Same function as [.setHintTextColor]. (The built-in one is a final method that can't be overridden, so use this method instead.)
     */
    fun setMetHintTextColor(colors: ColorStateList?) {
        textColorHintStateList = colors
        resetHintTextColor()
    }

    private fun resetHintTextColor() {
        if (textColorHintStateList == null) {
            setHintTextColor(baseColor and 0x00ffffff or 0x44000000)
        } else {
            setHintTextColor(textColorHintStateList)
        }
    }

    private fun setFloatingLabelInternal(mode: Int) {
        when (mode) {
            FLOATING_LABEL_NORMAL -> {
                floatingLabelEnabled = true
                highlightFloatingLabel = false
            }
            FLOATING_LABEL_HIGHLIGHT -> {
                floatingLabelEnabled = true
                highlightFloatingLabel = true
            }
            else -> {
                floatingLabelEnabled = false
                highlightFloatingLabel = false
            }
        }
    }

    fun setFloatingLabel(@FloatingLabelType mode: Int) {
        setFloatingLabelInternal(mode)
        initPadding()
    }

    fun getFloatingLabelPadding(): Int {
        return floatingLabelPadding
    }

    fun setFloatingLabelPadding(padding: Int) {
        floatingLabelPadding = padding
        postInvalidate()
    }

    fun setSingleLineEllipsis() {
        setSingleLineEllipsis(true)
    }

    fun setSingleLineEllipsis(enabled: Boolean) {
        singleLineEllipsis = enabled
        initMinBottomLines()
        initPadding()
        postInvalidate()
    }

    fun getMaxCharacters(): Int {
        return maxCharacters
    }

    fun setMaxCharacters(max: Int) {
        maxCharacters = max
        initMinBottomLines()
        initPadding()
        postInvalidate()
    }

    fun getMinCharacters(): Int {
        return minCharacters
    }

    fun setMinCharacters(min: Int) {
        minCharacters = min
        initMinBottomLines()
        initPadding()
        postInvalidate()
    }

    fun getMinBottomTextLines(): Int {
        return minBottomTextLines
    }

    fun setMinBottomTextLines(lines: Int) {
        minBottomTextLines = lines
        initMinBottomLines()
        initPadding()
        postInvalidate()
    }

    fun isAutoValidate(): Boolean {
        return autoValidate
    }

    fun setAutoValidate(autoValidate: Boolean) {
        this.autoValidate = autoValidate
        if (autoValidate) {
            validate()
        }
    }

    fun getErrorColor(): Int {
        return errorColor
    }

    fun setErrorColor(color: Int) {
        errorColor = color
        postInvalidate()
    }

    fun setHelperText(helperText: CharSequence?) {
        this.helperText = helperText?.toString()
        if (adjustBottomLines()) {
            postInvalidate()
        }
    }

    fun getHelperText(): String? {
        return helperText
    }

    fun getHelperTextColor(): Int {
        return helperTextColor
    }

    fun setHelperTextColor(color: Int) {
        helperTextColor = color
        postInvalidate()
    }

    override fun setError(errorText: CharSequence) {
        tempErrorText = if (errorText.isBlank()) null else errorText.toString()
        if (adjustBottomLines()) {
            postInvalidate()
        }
    }

    override fun getError(): CharSequence {
        return tempErrorText!!
    }

    /**
     * only used to draw the bottom line
     */
    private val isInternalValid: Boolean
        private get() = tempErrorText == null && isCharactersCountValid

    /**
     * if the main text matches the regex
     *
     */
    @Deprecated("use the new validator interface to add your own custom validator")
    fun isValid(regex: String?): Boolean {
        if (regex == null) {
            return false
        }
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(text)
        return matcher.matches()
    }

    /**
     * check if the main text matches the regex, and set the error text if not.
     *
     * @return true if it matches the regex, false if not.
     */
    @Deprecated("use the new validator interface to add your own custom validator")
    fun validate(regex: String?, errorText: CharSequence): Boolean {
        val isValid = isValid(regex)
        if (!isValid) {
            error = errorText
        }
        postInvalidate()
        return isValid
    }

    /**
     * Run validation on a single validator instance
     *
     * @param validator Validator to check
     * @return True if valid, false if not
     */
    fun validateWith(validator: METValidator): Boolean {
        val text: CharSequence? = text
        val isValid = validator.isValid(text!!, text.length == 0)
        if (!isValid) {
            error = validator.errorMessage
        }
        postInvalidate()
        return isValid
    }

    /**
     * Check all validators, sets the error text if not
     *
     *
     * NOTE: this stops at the first validator to report invalid.
     *
     * @return True if all validators pass, false if not
     */
    fun validate(): Boolean {
        if (validators == null || validators!!.isEmpty()) {
            return true
        }
        val text: CharSequence? = text
        val isEmpty = text!!.length == 0
        var isValid = true
        for (validator in validators!!) {
            isValid = isValid && validator.isValid(text, isEmpty)
            if (!isValid) {
                error = validator.errorMessage
                break
            }
        }
        if (isValid) {
            setError("")
        }
        postInvalidate()
        return isValid
    }

    fun hasValidators(): Boolean {
        return validators != null && !validators!!.isEmpty()
    }

    /**
     * Adds a new validator to the View's list of validators
     *
     *
     * This will be checked with the others in [.validate]
     *
     * @param validator Validator to add
     * @return This instance, for easy chaining
     */
    fun addValidator(validator: METValidator): MaterialEditText {
        if (validators == null) {
            validators = ArrayList()
        }
        validators!!.add(validator)
        return this
    }

    fun clearValidators() {
        if (validators != null) {
            validators!!.clear()
        }
    }

    fun getValidators(): List<METValidator>? {
        return validators
    }

    fun setLengthChecker(lengthChecker: METLengthChecker?) {
        this.lengthChecker = lengthChecker
    }

    override fun setOnFocusChangeListener(listener: OnFocusChangeListener) {
        if (innerFocusChangeListener == null) {
            super.setOnFocusChangeListener(listener)
        } else {
            outerFocusChangeListener = listener
        }
    }

    private fun getLabelAnimator(): ObjectAnimator? {
        if (labelAnimator == null) {
            labelAnimator = ObjectAnimator.ofFloat(this, "floatingLabelFraction", 0f, 1f)
        }
        labelAnimator!!.duration = if (isFloatingLabelAnimating) 300 else 0.toLong()
        return labelAnimator
    }

    private fun getLabelFocusAnimator(): ObjectAnimator? {
        if (labelFocusAnimator == null) {
            labelFocusAnimator = ObjectAnimator.ofFloat(this, "focusFraction", 0f, 1f)
        }
        return labelFocusAnimator
    }

    private fun getBottomLinesAnimator(destBottomLines: Float): ObjectAnimator? {
        if (bottomLinesAnimator == null) {
            bottomLinesAnimator = ObjectAnimator.ofFloat(this, "currentBottomLines", destBottomLines)
        } else {
            bottomLinesAnimator!!.cancel()
            bottomLinesAnimator!!.setFloatValues(destBottomLines)
        }
        return bottomLinesAnimator
    }

    override fun onDraw(canvas: Canvas) {
        val startX = scrollX + (if (iconLeftBitmaps == null) 0 else iconOuterWidth + iconPadding) + paddingLeft
        val endX = scrollX + (if (iconRightBitmaps == null) width else width - iconOuterWidth - iconPadding) - paddingRight
        var lineStartY = scrollY + height - paddingBottom

        LogUtils.d(tag,"startX = $startX, endX = $endX , iconOuterWidth = $iconOuterWidth,iconRightBitmaps == null is ${iconRightBitmaps == null}")
        // draw the icon(s)
        paint.alpha = 255
        if (iconLeftBitmaps != null) {
            val icon = iconLeftBitmaps!![if (!isInternalValid) 3 else if (!isEnabled) 2 else if (hasFocus()) 1 else 0]
            val iconLeft = startX - iconPadding - iconOuterWidth + (iconOuterWidth - icon!!.width) / 2
            val iconTop = lineStartY + bottomSpacing - iconOuterHeight + (iconOuterHeight - icon.height) / 2
            canvas.drawBitmap(icon, iconLeft.toFloat(), iconTop.toFloat(), paint)
        }
        if (iconRightBitmaps != null) {
            val icon = iconRightBitmaps!![if (!isInternalValid) 3 else if (!isEnabled) 2 else if (hasFocus()) 1 else 0]
            val iconRight = endX + iconPadding + (iconOuterWidth - icon!!.width) / 2
            val iconTop = lineStartY + bottomSpacing - iconOuterHeight + (iconOuterHeight - icon.height) / 2
            canvas.drawBitmap(icon, iconRight.toFloat(), iconTop.toFloat(), paint)
        }

        // draw the clear button
        if (hasFocus() && showClearButton && !TextUtils.isEmpty(text) && isEnabled) {
            paint.alpha = 255
            var buttonLeft: Int = if (isRTL) {
                startX
            } else {
                endX - iconOuterWidth
            }
            val clearButtonBitmap = clearButtonBitmaps!![0]
            buttonLeft += (iconOuterWidth - clearButtonBitmap!!.width) / 2
//            var iconTop = lineStartY + bottomSpacing - iconOuterHeight + (iconOuterHeight - clearButtonBitmap.height) / 2
            var iconTop = (clearButtonBitmap.height + (lineStartY - bottomSpacing ) /2 ) / 2
            LogUtils.d(tag,"lineStartY = $lineStartY,bottomSpacing = $bottomSpacing" +
                    ",iconOuterHeight = $iconOuterHeight" +
                    ",clearButtonBitmap.height = ${clearButtonBitmap.height}"
                    + ",(iconOuterHeight - clearButtonBitmap.height) / 2 = ${(iconOuterHeight - clearButtonBitmap.height) / 2}"
                    + ",iconTop = $iconTop")
            canvas.drawBitmap(clearButtonBitmap, buttonLeft.toFloat(), iconTop.toFloat(), paint)
        }

        // draw the secret button
        if (hasFocus() && showSecretButton && !TextUtils.isEmpty(text) && isEnabled) {
            paint.alpha = 255
            var buttonLeft: Int = if (isRTL) {
                startX
            } else {
                endX - iconOuterWidth
            }
            var secretButtonBitmap = secretOffButtonBitmaps!![0]
            if (secretVisibility) {
                secretButtonBitmap = secretOnButtonBitmaps!![0]
            }
            buttonLeft += (iconOuterWidth - secretButtonBitmap!!.width) / 2
//            val iconTop = lineStartY + bottomSpacing - iconOuterHeight + (iconOuterHeight - secretButtonBitmap.height) / 2
            var iconTop = (secretButtonBitmap.height + (lineStartY - bottomSpacing ) /2 ) / 2
            canvas.drawBitmap(secretButtonBitmap, buttonLeft.toFloat(), iconTop.toFloat(), paint)
        }

        // draw the underline
        if (!hideUnderline) {
            lineStartY += bottomSpacing
            if (!isInternalValid) { // not valid
                paint.color = errorColor
                canvas.drawRect(startX.toFloat(), lineStartY.toFloat(), endX.toFloat(), (lineStartY + getPixel(2)).toFloat(), paint)
            } else if (!isEnabled) { // disabled
                paint.color = if (underlineColor != -1) underlineColor else baseColor and 0x00ffffff or 0x44000000
                val interval = getPixel(1).toFloat()
                var xOffset = 0f
                while (xOffset < width) {
                    canvas.drawRect(startX + xOffset, lineStartY.toFloat(), startX + xOffset + interval, (lineStartY + getPixel(1)).toFloat(), paint)
                    xOffset += interval * 3
                }
            } else if (hasFocus()) { // focused
                paint.color = primaryColor
                canvas.drawRect(startX.toFloat(), lineStartY.toFloat(), endX.toFloat(), (lineStartY + getPixel(2)).toFloat(), paint)
            } else { // normal
                paint.color = if (underlineColor != -1) underlineColor else baseColor and 0x00ffffff or 0x1E000000
                canvas.drawRect(startX.toFloat(), lineStartY.toFloat(), endX.toFloat(), (lineStartY + getPixel(1)).toFloat(), paint)
            }
        }
        textPaint.textSize = bottomTextSize.toFloat()
        val textMetrics = textPaint.fontMetrics
        val relativeHeight = -textMetrics.ascent - textMetrics.descent
        val bottomTextPadding = bottomTextSize + textMetrics.ascent + textMetrics.descent

        // draw the characters counter
        if (hasFocus() && hasCharactersCounter() || !isCharactersCountValid) {
            textPaint.color = if (isCharactersCountValid) baseColor and 0x00ffffff or 0x44000000 else errorColor
            val charactersCounterText = charactersCounterText
            canvas.drawText(charactersCounterText, (if (isRTL) startX else endX - textPaint.measureText(charactersCounterText)) as Float, lineStartY + bottomSpacing + relativeHeight, textPaint)
        }

        // draw the bottom text
        if (textLayout != null) {
            if (tempErrorText != null || (helperTextAlwaysShown || hasFocus()) && !TextUtils.isEmpty(helperText)) { // error text or helper text
                textPaint.color = if (tempErrorText != null) errorColor else if (helperTextColor != -1) helperTextColor else baseColor and 0x00ffffff or 0x44000000
                canvas.save()
                if (isRTL) {
                    canvas.translate((endX - textLayout!!.width).toFloat(), lineStartY + bottomSpacing - bottomTextPadding)
                } else {
                    canvas.translate((startX + bottomTextLeftOffset).toFloat(), lineStartY + bottomSpacing - bottomTextPadding)
                }
                textLayout!!.draw(canvas)
                canvas.restore()
            }
        }

        // draw the floating label
        if (floatingLabelEnabled && !TextUtils.isEmpty(floatingLabelText)) {
            textPaint.textSize = floatingLabelTextSize.toFloat()
            // calculate the text color
            textPaint.color = (focusEvaluator.evaluate(focusFraction * if (isEnabled) 1 else 0, if (floatingLabelTextColor != -1) floatingLabelTextColor else baseColor and 0x00ffffff or 0x44000000, primaryColor) as Int)

            // calculate the horizontal position
            val floatingLabelWidth = textPaint.measureText(floatingLabelText.toString())
            val floatingLabelStartX: Int = if (gravity and Gravity.RIGHT == Gravity.RIGHT || isRTL) {
                (endX - floatingLabelWidth).toInt()
            } else if (gravity and Gravity.LEFT == Gravity.LEFT) {
                startX
            } else {
                startX + (innerPaddingLeft + (width - innerPaddingLeft - innerPaddingRight - floatingLabelWidth) / 2).toInt()
            }

            // calculate the vertical position
            val distance = floatingLabelPadding
            val floatingLabelStartY = (innerPaddingTop + floatingLabelTextSize + floatingLabelPadding - distance * (if (floatingLabelAlwaysShown) 1f else floatingLabelFraction) + scrollY)

            // calculate the alpha
            val alpha = ((if (floatingLabelAlwaysShown) 1f else floatingLabelFraction) * 0xff * (0.74f * focusFraction * (if (isEnabled) 1f else 0f) + 0.26f) * if (floatingLabelTextColor != -1) 1f else Color.alpha(floatingLabelTextColor) / 256f)
            textPaint.alpha = alpha.toInt()

            // draw the floating label
            canvas.drawText(floatingLabelText.toString(), floatingLabelStartX.toFloat(), floatingLabelStartY, textPaint)
        }

        // draw the bottom ellipsis
        if (hasFocus() && singleLineEllipsis && scrollX != 0) {
            paint.color = if (isInternalValid) primaryColor else errorColor
            val startY = (lineStartY + bottomSpacing).toFloat()
            val ellipsisStartX: Int = if (isRTL) {
                endX
            } else {
                startX
            }
            val signum = if (isRTL) -1 else 1
            canvas.drawCircle((ellipsisStartX + signum * bottomEllipsisSize / 2).toFloat(), startY + bottomEllipsisSize / 2, (bottomEllipsisSize / 2).toFloat(), paint)
            canvas.drawCircle((ellipsisStartX + signum * bottomEllipsisSize * 5 / 2).toFloat(), startY + bottomEllipsisSize / 2, (bottomEllipsisSize / 2).toFloat(), paint)
            canvas.drawCircle((ellipsisStartX + signum * bottomEllipsisSize * 9 / 2).toFloat(), startY + bottomEllipsisSize / 2, (bottomEllipsisSize / 2).toFloat(), paint)
        }

        // draw the original things
        super.onDraw(canvas)
    }

    @get:TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private val isRTL: Boolean
        private get() {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                return false
            }
            val config = resources.configuration
            return config.layoutDirection == LAYOUT_DIRECTION_RTL
        }
    private val bottomTextLeftOffset: Int
        private get() = if (isRTL) charactersCounterWidth else bottomEllipsisWidth
    private val bottomTextRightOffset: Int
        private get() = if (isRTL) bottomEllipsisWidth else charactersCounterWidth
    private val charactersCounterWidth: Int
        private get() = if (hasCharactersCounter()) textPaint.measureText(charactersCounterText).toInt() else 0
    private val bottomEllipsisWidth: Int
        private get() = if (singleLineEllipsis) bottomEllipsisSize * 5 + getPixel(4) else 0

    private fun checkCharactersCount() {
        if (!firstShown && !checkCharactersCountAtBeginning || !hasCharactersCounter()) {
            isCharactersCountValid = true
        } else {
            val text: CharSequence? = text
            val count = text?.let { checkLength(it) } ?: 0
            isCharactersCountValid = count >= minCharacters && (maxCharacters <= 0 || count <= maxCharacters)
        }
    }

    private fun hasCharactersCounter(): Boolean {
        return minCharacters > 0 || maxCharacters > 0
    }

    private val charactersCounterText: String
        private get() {
            return if (minCharacters <= 0) {
                if (isRTL) maxCharacters.toString() + " / " + checkLength(getText()) else checkLength(getText()).toString() + " / " + maxCharacters
            } else if (maxCharacters <= 0) {
                if (isRTL) "+" + minCharacters + " / " + checkLength(getText()) else checkLength(getText()).toString() + " / " + minCharacters + "+"
            } else {
                if (isRTL) maxCharacters.toString() + "-" + minCharacters + " / " + checkLength(getText()) else checkLength(getText()).toString() + " / " + minCharacters + "-" + maxCharacters
            }
        }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (singleLineEllipsis && scrollX > 0 && event.action == MotionEvent.ACTION_DOWN && event.x < getPixel(4 * 5) && event.y > height - extraPaddingBottom - innerPaddingBottom && event.y < height - innerPaddingBottom) {
            setSelection(0)
            return false
        }
        if (hasFocus() && showClearButton && isEnabled) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (insideClearButton(event)) {
                        clearButtonTouched = true
                        clearButtonClicking = true
                        return true
                    }
                    if (clearButtonClicking && !insideClearButton(event)) {
                        clearButtonClicking = false
                    }
                    if (clearButtonTouched) {
                        return true
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    if (clearButtonClicking && !insideClearButton(event)) {
                        clearButtonClicking = false
                    }
                    if (clearButtonTouched) {
                        return true
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if (clearButtonClicking) {
                        if (!TextUtils.isEmpty(text)) {
                            text = null
                        }
                        clearButtonClicking = false
                    }
                    if (clearButtonTouched) {
                        clearButtonTouched = false
                        return true
                    }
                    clearButtonTouched = false
                }
                MotionEvent.ACTION_CANCEL -> {
                    clearButtonTouched = false
                    clearButtonClicking = false
                }
            }
        }
        if (hasFocus() && showSecretButton && isEnabled) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (insideClearButton(event)) {
                        secretButtonTouched = true
                        secretButtonClicking = true
                        return true
                    }
                    if (secretButtonClicking && !insideClearButton(event)) {
                        secretButtonClicking = false
                    }
                    if (secretButtonTouched) {
                        return true
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    if (secretButtonClicking && !insideClearButton(event)) {
                        secretButtonClicking = false
                    }
                    if (secretButtonTouched) {
                        return true
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if (secretButtonClicking) {
                        if (!TextUtils.isEmpty(text)) {
                            switchSecretVisibility()
                        }
                        secretButtonClicking = false
                    }
                    if (secretButtonTouched) {
                        secretButtonTouched = false
                        return true
                    }
                    secretButtonTouched = false
                }
                MotionEvent.ACTION_CANCEL -> {
                    secretButtonTouched = false
                    secretButtonClicking = false
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun switchSecretVisibility() {
        val selectionEnd = this.selectionEnd
        if (secretVisibility) {
            this.transformationMethod = PasswordTransformationMethod.getInstance()
        } else {
            this.transformationMethod = HideReturnsTransformationMethod.getInstance()
        }
        secretVisibility = !secretVisibility
        this.setSelection(selectionEnd)
    }

    private fun insideClearButton(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        val startX = scrollX + if (iconLeftBitmaps == null) 0 else iconOuterWidth + iconPadding
        val endX = scrollX + if (iconRightBitmaps == null) width else width - iconOuterWidth - iconPadding
        val buttonLeft: Int = if (isRTL) {
            startX
        } else {
            endX - iconOuterWidth
        }
        val buttonTop = scrollY + height - paddingBottom + bottomSpacing - iconOuterHeight
        return x >= buttonLeft && x < buttonLeft + iconOuterWidth && y >= buttonTop && y < buttonTop + iconOuterHeight
    }

    private fun checkLength(text: CharSequence?): Int {
        return if (lengthChecker == null) text!!.length else lengthChecker!!.getLength(text)
    }

    companion object {
        const val FLOATING_LABEL_NONE = 0
        const val FLOATING_LABEL_NORMAL = 1
        const val FLOATING_LABEL_HIGHLIGHT = 2
    }
}