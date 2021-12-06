//package com.qschou.pedulisehat.android.utils
//
//import android.app.Activity
//import android.graphics.*
//import android.view.Gravity
//import android.view.View
//import android.widget.TextView
//import androidx.constraintlayout.widget.ConstraintLayout
//import com.s1rem.gudangview.adapter.BaseCard
//
///**
// *   Created by 5Mall<zhangwei> on 2019-08-22
// *   Email:zhangwei@qingsongchou.com
// *   描述：
// */
//
//
//data class GuideView(val view: View, val stringResId: Int, val padding: BaseCard.Padding = Padding(0, 0, 0, 0), val arrowHorizontalBias: Float = 0.15f, val arrowUp: Boolean = true, val skipBtnTop: Boolean = true)
//
//typealias OnDismissListener = () -> Unit
//
//class GuideMaskManager(private val activity: Activity, private val guideViews: Array<GuideView?>) : OnDrawHighLightCallback {
//
//    private val dp10 by lazy { DensityUtil.dp2Px(activity, 10f) }
//    private val dp6 by lazy { DensityUtil.dp2Px(activity, 6f) }
//    private val strokePaint by lazy {
//        val strokePaint = Paint()
//        // 在此进行自定义绘制
//        strokePaint.style = Paint.Style.STROKE
//        strokePaint.isAntiAlias = true
//        strokePaint.strokeWidth = DensityUtil.dp2Px(1f)
//        strokePaint.color = Color.WHITE
//        strokePaint.pathEffect = DashPathEffect(floatArrayOf(DensityUtil.dp2Px(5f), dp6), 0f)
//        strokePaint
//    }
//    private var curIndex = 0
//    private val rootView by lazy { activity.findViewById<View>(android.R.id.content) }
//
//    /**
//     * canvas: 蒙层的画布
//     * rect:   高亮块
//     */
//    override fun invoke(canvas: Canvas, rect: RectF, paint: Paint) {
//        // 绘制的画笔。模式为PorterDuff.Mode.CLEAR。
//        val padding = DensityUtil.dp2Px(3f)
//        canvas.save()
//        canvas.drawRoundRect(RectF(rect.left + padding, rect.top + padding, rect.right - padding, rect.bottom - padding), dp6, dp6, paint)
//        canvas.drawRoundRect(rect, dp6, dp6, strokePaint)
//        canvas.restore()
//    }
//
//
//    fun launch(onDismiss: OnDismissListener? = null) {
//        if (guideViews.isEmpty()) return
//
//        val layer = EasyGuideLayer.with(activity)
//                .setBackgroundColor(0xCC000000.toInt())// 蒙层背景色
////                .setDismissOnClickOutside(false)// 设置点击到蒙层上的非点击区域时，是否自动让蒙层消失
//                .setDismissIfNoItems(true)// 设置当蒙层中一个引导层实例都没绑定时，自动让蒙层消失
//                .setOnClickOutsideListener { _, controller ->
//                    if (curIndex < guideViews.size - 1) {
//                        controller.getGuideLayer().apply {
//                            clearItems()
//                            showGuideItem(curIndex + 1, this, rootView, onDismiss)
//                        }
//                    } else {
//                        controller.dismiss()
//                        onDismiss?.invoke()
//                    }
//                }
//
//        showGuideItem(curIndex, layer, rootView, onDismiss)
//    }
//
//    private fun showGuideItem(index: Int, glayer: EasyGuideLayer, rootView: View, onDismiss: OnDismissListener? = null) {
//
//        val guideView = guideViews.getOrNull(index)
//        if (guideView == null) {
//            if (index < guideViews.size - 1) {
//                showGuideItem(index + 1, glayer, rootView)
//            } else {
//                glayer.show()
//            }
//            return
//        }
//
//        val ip = if (guideView.arrowUp) R.layout.view_guide_content_up to Gravity.BOTTOM else R.layout.view_guide_content_down to Gravity.TOP
//
//        val item = GuideItem.newInstance(guideView.view, guideView.padding)
//                .setLayout(ip.first)
//                .setGravity(ip.second)// 设置引导View位置
//                .setHighLightShape(GuideItem.SHAPE_RECT)// 设置高亮区域绘制模式
//                .setOnDrawHighLightCallback(this)
//                .setOnViewAttachedListener { view, _ ->
//                    view.findViewById<TextView>(R.id.tvContent).setText(guideView.stringResId)
//                    view.findViewById<View>(R.id.ivTag)?.let {
//                        val params = it.layoutParams as ConstraintLayout.LayoutParams
//                        params.horizontalBias = guideView.arrowHorizontalBias
//                        it.layoutParams = params
//                    }
//                }
//                .setOffsetProvider { point: Point, // 顶点位置
//                                     _: RectF, // 高亮块区域
//                                     _: View ->
//                    // 具体的引导View实例。此处view已被测量大小。可以直接获取width、height数据
//                    point.x = 0
//                }
//
//        val bp = if (guideView.skipBtnTop) RectF(rootView.right - 8 * dp10, 0f, 0f, 3 * dp10) to Gravity.BOTTOM else RectF(rootView.right - 8 * dp10, rootView.bottom - 4 * dp10, 0f, 0f) to Gravity.TOP
//
//        val button = GuideItem.newInstance(bp.first)
//                .setLayout(R.layout.view_guide_button)
//                .setGravity(bp.second)// 设置引导View位置
//                .setHighLightShape(GuideItem.SHAPE_NONE)// 设置高亮区域绘制模式
//                .setOnViewAttachedListener { view, controller ->
//                    view.setOnClickListener {
//                        controller.dismiss()
//                        onDismiss?.invoke()
//                    }
//                }
//
//        glayer.addItem(item)
//        glayer.addItem(button)
//        glayer.show()
//        curIndex = index
//    }
//}
