package com.m.k.mvp.widgets

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.*
import com.m.k.mvp.R

/*
 * created by Cherry on 2019-11-24
 **/
class MarqueeView : HorizontalScrollView {


    constructor(context: Context) : this(context,null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        initAttrs(context, attrs)
        initLayout()
        initAnim()
    }


    private var mTextView: TextView? = null
    private var mGhostTextView: TextView? = null

    private var viewWidth: Int = 0

    private var mText: CharSequence? = null

    private var measureText: Int = 0

    private var textColor = -0x1000000

    private var mOffset = 0
    private var mGhostOffset = 0


    private var spacing = 100

    private var speed = 1

    private var valueAnimator: ValueAnimator? = null

    private var textSize = 13


    @JvmSuppressWildcards
   private var onMarqueeTextClickListener : OnMarqueeTextClickListener<MarqueeData>? = null;

    private var animatorUpdateListener: ValueAnimator.AnimatorUpdateListener = ValueAnimator.AnimatorUpdateListener {
        mOffset -= speed
        mGhostOffset -= speed
        if (mOffset + measureText < 0) {
            mOffset = mGhostOffset + measureText + spacing
        }
        if (mGhostOffset + measureText < 0) {
            mGhostOffset = mOffset + measureText + spacing
        }
        invalidate()

    }


    public fun  setOnMarqueeTextClickListener(listener : OnMarqueeTextClickListener<MarqueeData>){
        onMarqueeTextClickListener = listener
    }

    @SuppressLint("Recycle")
    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MarqueeView)
        textColor = typedArray.getColor(R.styleable.MarqueeView_textColor, textColor)
        if (typedArray.hasValue(R.styleable.MarqueeView_textSize)) {
            textSize = typedArray.getDimension(R.styleable.MarqueeView_textSize, textSize.toFloat()).toInt()
            textSize = px2sp(context, textSize.toFloat())
        }

        if (typedArray.hasValue(R.styleable.MarqueeView_spacing)) {
            spacing = typedArray.getDimension(R.styleable.MarqueeView_spacing, spacing.toFloat()).toInt()
        }

        if (typedArray.hasValue(R.styleable.MarqueeView_speed)) {
            speed = typedArray.getInteger(R.styleable.MarqueeView_speed, 2)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        viewWidth = measuredWidth
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (measureText > viewWidth) {
            startAnim()
        } else {
            stopAnim()
        }
    }

    private fun initLayout() {
        val relativeLayout = RelativeLayout(context)
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        relativeLayout.layoutParams = layoutParams
        addView(relativeLayout)

        mTextView = createTextView()
        mGhostTextView = createTextView()

        relativeLayout.addView(mTextView)
        relativeLayout.addView(mGhostTextView)
    }

    private fun initAnim() {
        valueAnimator = ValueAnimator.ofFloat(0f, measureText.toFloat())
        valueAnimator!!.addUpdateListener(animatorUpdateListener)
        valueAnimator!!.repeatCount = ValueAnimator.INFINITE
        valueAnimator!!.repeatMode = ValueAnimator.RESTART
    }

    fun setSpacing(spacing: Int) {
        this.spacing = spacing
    }

    fun setSpeed(speed: Int) {
        this.speed = speed
    }


    fun setClickableText(list: List<MarqueeData>?) {

        if (list == null || list.isEmpty()) {
            return
        }
        mTextView!!.highlightColor = resources.getColor(android.R.color.transparent)
        mGhostTextView!!.highlightColor = resources.getColor(android.R.color.transparent)

        val builder = SpannableStringBuilder()
        var count = 0
        for (marqueeData in list) {
            builder.append(setClickableSpan(marqueeData,count++)).append("    ")
        }
        setText(builder)
    }

   private fun setClickableSpan(data: MarqueeData,position : Int): SpannableString {

        val string = SpannableString(data.getString())
        val span = object : ClickableSpan() {
            override fun onClick(widget: View) {
                onMarqueeTextClickListener?.onClick(data,position)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                // 设置显示的用户名文本颜色
                ds.color = Color.parseColor("#FF9B9C9E")
                ds.isUnderlineText = false
            }
        }

        string.setSpan(span, 0, string.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return string
    }

    fun setText(text: CharSequence) {
        this.mText = text

        mTextView!!.text = text
        mGhostTextView!!.text = mText

        measureText = mTextView!!.paint.measureText(mText, 0, mText!!.length).toInt()
        resetMarqueeView()
        if (measureText > viewWidth) {
            startAnim()
        } else {
            stopAnim()
        }
    }

    private fun createTextView(): TextView {
        val textView = TextView(context)
        textView.setPadding(0, 0, 0, 0)
        textView.setSingleLine()
        textView.setTextColor(textColor)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12f);


        val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        textView.layoutParams = layoutParams
        textView.gravity = Gravity.CENTER_VERTICAL
        textView.movementMethod = LinkMovementMethod.getInstance()
        return textView
    }

    private fun resetMarqueeView() {
        mOffset = 0
        mGhostOffset = measureText + spacing
        mGhostTextView!!.x = mGhostOffset.toFloat()
        invalidate()
    }



   private fun startAnim() {
        valueAnimator!!.duration = measureText.toLong()
        stopAnim()
        valueAnimator!!.start()
    }

    private fun stopAnim() {
        valueAnimator!!.cancel()
        resetMarqueeView()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mTextView == null || mGhostTextView == null) {
            return
        }
        mTextView!!.x = mOffset.toFloat()
        mGhostTextView!!.x = mGhostOffset.toFloat()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return true
    }



    interface MarqueeData {

       fun  getString() : String

    }


    @JvmSuppressWildcards
    interface OnMarqueeTextClickListener<T> {

        fun onClick(data : T, position : Int)
    }

    companion object {

        fun px2sp(context: Context, pxValue: Float): Int {
            val fontScale = context.resources.displayMetrics.scaledDensity
            return (pxValue / fontScale + 0.5f).toInt()
        }
    }
}
