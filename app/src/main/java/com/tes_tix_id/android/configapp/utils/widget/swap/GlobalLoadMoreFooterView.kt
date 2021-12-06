package com.tes_tix_id.android.configapp.utils.widget.swap

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.aspsine.swipetoloadlayout.SwipeLoadMoreTrigger
import com.aspsine.swipetoloadlayout.SwipeTrigger
import com.tes_tix_id.android.R

class GlobalLoadMoreFooterView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr), SwipeTrigger, SwipeLoadMoreTrigger {
    private var linePaint: Paint? = null
    private var textPaint: Paint? = null
    private var text: String? = null
    val release_to_load: String
    val pull_up_to_load: String
    val load_completed: String
    val loading: String
    private fun init() {
        textPaint = Paint()
        textPaint!!.isAntiAlias = true
        textPaint!!.color = -0x555556
        textPaint!!.textSize = sp2px(12f)
        textPaint!!.textAlign = Paint.Align.CENTER
        linePaint = Paint()
        linePaint!!.isAntiAlias = true
        linePaint!!.color = -0x777778
    }

    override fun onLoadMore() {
        setText(loading)
    }

    override fun onPrepare() {
        linePaint!!.color = -0x777778
        setText(pull_up_to_load)
    }

    //上拉加载／释放加载／加载中
    override fun onMove(yScrolled: Int, isComplete: Boolean, automatic: Boolean) {
        if (!isComplete) {
            if (yScrolled <= -height) {
                setText(release_to_load)
            } else {
                setText(pull_up_to_load)
            }
        } else {
            setText(load_completed)
        }
    }

    override fun onRelease() {
//        setText(loading);
    }

    override fun onComplete() {
        linePaint!!.color = 0x00888888
        setText(load_completed)
    }

    override fun onReset() {
        linePaint!!.color = -0x777778
        setText(pull_up_to_load)
    }

    private fun setText(text: String) {
        this.text = text
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var width = suggestedMinimumWidth
        var height = suggestedMinimumHeight
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize
        }
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawLine(0f, 0f, width.toFloat(), 0f, linePaint!!)
        val x = (width / 2).toFloat()
        val y = (height - (textPaint!!.descent() + textPaint!!.ascent())) / 2
        canvas.drawText(text!!, x, y, textPaint!!)
    }

    /**
     * dp 2 px
     *
     * @param dpVal
     */
    protected fun sp2px(dpVal: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                dpVal, resources.displayMetrics)
    }

    init {
        release_to_load = resources.getString(R.string.release_to_load)
        pull_up_to_load = resources.getString(R.string.pull_up_to_load)
        load_completed = resources.getString(R.string.load_completed)
        loading = resources.getString(R.string.app_loading)
        init()
    }
}