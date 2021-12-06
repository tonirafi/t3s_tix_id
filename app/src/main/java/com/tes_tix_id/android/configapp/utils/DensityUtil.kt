package com.tes_tix_id.android.configapp.utils

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics

class DensityUtil private constructor() {
    companion object {
        fun dp2px(context: Context, dpVal: Float): Int {
            val scale = getDisplayMetrics(context).density
            return (dpVal * scale + 0.5f).toInt()
        }

        fun dp2Px(context: Context, dpVal: Float): Float {
            val scale = getDisplayMetrics(context).density
            return dpVal * scale
        }

        fun px2dp(context: Context, pxVal: Float): Float {
            val scale = getDisplayMetrics(context).density
            return pxVal / scale
        }

        fun sp2px(context: Context, spVal: Float): Int {
            val fontScale = getDisplayMetrics(context).scaledDensity
            return (spVal * fontScale + 0.5f).toInt()
        }

        fun px2sp(context: Context, pxVal: Float): Float {
            return pxVal / getDisplayMetrics(context).scaledDensity
        }

        fun dp2px(dpVal: Float): Int {
            val scale = getDisplayMetrics(null).density
            return (dpVal * scale + 0.5f).toInt()
        }

        fun dp2Px(dpVal: Float): Float {
            val scale = getDisplayMetrics(null).density
            return dpVal * scale
        }

        fun px2dp(pxVal: Float): Float {
            val scale = getDisplayMetrics(null).density
            return pxVal / scale
        }

        fun px2sp(pxVal: Float): Float {
            return pxVal / getDisplayMetrics(null).scaledDensity
        }

        fun sp2px(spVal: Float): Int {
            val fontScale = getDisplayMetrics(null).scaledDensity
            return (spVal * fontScale + 0.5f).toInt()
        }

        fun getDpOfSWR(ratio: Float): Float {
            return getDisplayMetrics(null).widthPixels * ratio
        }

        private fun getDisplayMetrics(context: Context?): DisplayMetrics {
            return if (context == null) Resources.getSystem().displayMetrics else context.resources.displayMetrics
        }
    }

    init {
        throw UnsupportedOperationException("cannot be instantiated")
    }
}