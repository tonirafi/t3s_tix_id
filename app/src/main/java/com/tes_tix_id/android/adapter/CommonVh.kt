package com.tes_tix_id.android.adapter

import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import com.tes_tix_id.android.adapter.BaseCard.Companion.newMargin
import com.tes_tix_id.android.adapter.BaseCard.Companion.newPadding
import com.tes_tix_id.android.adapter.BaseCard.Margin
import me.samlss.broccoli.Broccoli
import me.samlss.broccoli.BroccoliGradientDrawable
import me.samlss.broccoli.PlaceholderParameter

open class CommonVh<T : BaseCard?> @JvmOverloads constructor(itemView: View?, protected var onItemClickListener: CardAdapter.OnItemClickListener? = null) : RecyclerView.ViewHolder(itemView!!) {
    protected var baseCard: T? = null
    protected var init = false
    protected open fun onItemViewClick(v: View) {
        if (onItemClickListener != null && adapterPosition >= 0) {
            onItemClickListener!!.onItemOnclick(adapterPosition)
        }
        if (baseCard != null && !TextUtils.isEmpty(baseCard!!.uri)) {
//            IntentUtil.intentToUri(v.context, baseCard!!.uri, baseCard!!.uriRequestCode)
        }
    }

    fun setSelected(b: Boolean) {}
    open fun bind(card: T) {
        baseCard = card
        setPadding(card!!.padding)
        setMargin(card.margin)
        if (itemView.tag is Broccoli) {
            (itemView.tag as Broccoli).removeAllPlaceholders()
        }
    }

    /**
     * 展示占位图加载模式
     *
     * @param excludeViews 不显示占位图的view
     */
    protected fun showPlaceholders(vararg excludeViews: View?) {
        val broccoli = Broccoli()
        showPlaceholders(broccoli, itemView)
        if (excludeViews != null) {
            for (i in 0 until excludeViews.size) {
                removePlaceholder(broccoli, excludeViews[i])
            }
        }
        broccoli.show()
        itemView.tag = broccoli
    }

    private fun removePlaceholder(broccoli: Broccoli, v: View?) {
        if (v is ViewGroup) {
            for (i in 0 until v.childCount) {
                removePlaceholder(broccoli, v.getChildAt(i))
            }
        } else {
            broccoli.removePlaceholder(v)
        }
    }

    private fun showPlaceholders(broccoli: Broccoli, view: View) {
        if (view.visibility != View.VISIBLE) return
        if (view is ViewGroup) {
            val vp = view
            for (i in 0 until vp.childCount) {
                showPlaceholders(broccoli, vp.getChildAt(i))
            }
        } else {
            broccoli.addPlaceholder(PlaceholderParameter.Builder()
                    .setView(view)
                    .setDrawable(BroccoliGradientDrawable(Color.parseColor("#DDDDDD"),
                            Color.parseColor("#CCCCCC"), 0F, 1000, LinearInterpolator()))
                    .build())
        }
    }

    fun bind(card: T, payloads: List<*>) {
        baseCard = card
    }

    protected fun setPadding(padding: BaseCard.Padding?) {
        if (padding == null) {
            /** 针对背景图是 .9.png的情况 在此处调用的时候由于图片中包含的其padding数据有可能没有渲染进itemView 所以需要等itemView 包含padding数据的时候 进行操作 防止.9.png 图片中的padding 数据被覆盖   */
            if (itemView.paddingLeft > 0 || itemView.paddingTop > 0 || itemView.paddingRight > 0 || itemView.paddingBottom > 0) {
                baseCard!!.paddingFromLayout = true
                baseCard!!.padding = newPadding(itemView.paddingLeft, itemView.paddingTop, itemView.paddingRight, itemView.paddingBottom)
            }
            return
        }
        var changed = false
        if (itemView.paddingLeft != padding.left) {
            changed = true
        }
        if (itemView.paddingRight != padding.right) {
            changed = true
        }
        if (itemView.paddingBottom != padding.bottom) {
            changed = true
        }
        if (itemView.paddingTop != padding.top) {
            changed = true
        }
        if (changed) {
            itemView.setPadding(padding.left, padding.top, padding.right, padding.bottom)
        }
    }

    protected fun setMargin(margin: Margin?) {
        var changed = false
        val params: ViewGroup.MarginLayoutParams
        if (itemView.layoutParams == null) {
            changed = true
            params = ViewGroup.MarginLayoutParams(-1, -2)
        } else if (itemView.layoutParams is ViewGroup.MarginLayoutParams) {
            params = itemView.layoutParams as ViewGroup.MarginLayoutParams
        } else {
            changed = true
            params = ViewGroup.MarginLayoutParams(itemView.layoutParams.width, itemView.layoutParams.height)
            params.layoutAnimationParameters = itemView.layoutParams.layoutAnimationParameters
        }
        if (margin == null) {
            baseCard!!.margin = newMargin(params.leftMargin, params.topMargin, params.rightMargin, params.bottomMargin)
            if (changed) {
                itemView.layoutParams = params
            }
            return
        }
        if (params.leftMargin != margin.left) {
            changed = true
            params.leftMargin = margin.left
        }
        if (params.rightMargin != margin.right) {
            changed = true
            params.rightMargin = margin.right
        }
        if (params.bottomMargin != margin.bottom) {
            changed = true
            params.bottomMargin = margin.bottom
        }
        if (params.topMargin != margin.top) {
            changed = true
            params.topMargin = margin.top
        }
        if (changed) {
            itemView.layoutParams = params
        }
    }

    init {
        ButterKnife.bind(this, itemView!!)
        itemView.setOnClickListener { v: View -> onItemViewClick(v) }
    }
}