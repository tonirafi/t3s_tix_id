package com.tes_tix_id.android.configapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.tes_tix_id.android.MyApplication.Companion.getContext

object NetworkUtils {
    fun isConnected(context: Context): Boolean {
        val info = getActiveNetworkInfo(context.applicationContext)
        return info != null && info.isConnected
    }

    val isConnected: Boolean
        get() {
            val info = getActiveNetworkInfo(getContext())
            return info != null && info.isConnected
        }

    private fun getActiveNetworkInfo(context: Context): NetworkInfo? {
        val cm = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        //        return cm == null ? null : cm.getActiveNetworkInfo();
        return null
    }
}