package com.tes_tix_id.android.configapp.utils

import android.util.Log
import com.tes_tix_id.android.BuildConfig

object LogUtil {
    private const val TAG = "Gudangview"
    private val DEBUG = BuildConfig.DEBUG
    fun logW(msg: String?) {
        logW(TAG, msg)
    }

    fun logW(tag: String?, msg: String?) {
        if (DEBUG) {
            Log.w(tag, msg)
        }
    }

    fun logE(msg: String?) {
        logE(TAG, msg)
    }

    fun logE(tag: String?, msg: String?) {
        if (DEBUG) {
            Log.e(tag, msg)
        }
    }

    fun logD(msg: String?) {
        logD(TAG, msg)
    }

    fun logD(tag: String?, msg: String?) {
        if (DEBUG) {
            Log.d(tag, msg)
        }
    }

    fun logV(msg: String?) {
        logV(TAG, msg)
    }

    fun logV(tag: String?, msg: String?) {
        if (DEBUG) {
            Log.v(tag, msg)
        }
    }

    fun logI(msg: String?) {
        logI(TAG, msg)
    }

    fun logI(tag: String?, msg: String?) {
        if (DEBUG) {
            Log.i(tag, msg)
        }
    }
}