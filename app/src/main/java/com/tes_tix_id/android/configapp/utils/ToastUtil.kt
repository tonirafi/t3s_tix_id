package com.tes_tix_id.android.configapp.utils

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ProgressBar
import android.widget.Toast
import com.tes_tix_id.android.MyApplication.Companion.getContext
import com.tes_tix_id.android.R

object ToastUtil {
    private var sToast: Toast? = null
    fun show(content: String?) {
        show(getContext(), content)
    }

    fun show(resId: Int) {
        show(getContext(), resId)
    }

    fun showLong(content: String?) {
        showLong(getContext(), content)
    }

    fun showLong(resId: Int) {
        showLong(getContext(), resId)
    }

    fun show(context: Context, content: CharSequence?) {
        showToast(
            context,
            content,
            Toast.LENGTH_SHORT,
            Gravity.CENTER
        )!!.show()
    }

    fun show(context: Context, resId: Int) {
        showToast(
            context,
            context.getText(resId),
            Toast.LENGTH_SHORT,
            Gravity.CENTER
        )!!.show()
    }

    fun showLong(context: Context, content: CharSequence?) {
        showToast(
            context,
            content,
            Toast.LENGTH_LONG,
            Gravity.CENTER
        )!!.show()
    }

    fun showLong(context: Context, resId: Int) {
        showToast(
            context,
            context.getText(resId),
            Toast.LENGTH_LONG,
            Gravity.CENTER
        )!!.show()
    }

    fun showToast(context: Context, content: CharSequence?, duration: Int, gravity: Int): Toast? {
        if (sToast == null) {
//            sToast = Toast.makeText(context.getApplicationContext(), content, Toast.LENGTH_LONG);
            sToast = Toast(context.applicationContext)
            sToast!!.view =
                LayoutInflater.from(context.applicationContext).inflate(R.layout.layout_toast, null)
        }
        sToast!!.setText(content)
        if (sToast!!.duration != duration) {
            sToast!!.duration = duration
        }
        if (sToast!!.gravity != gravity) {
            sToast!!.setGravity(
                gravity,
                0,
                if (gravity == Gravity.CENTER) 0 else DensityUtil.dp2px(context, 66f)
            )
        }
        return sToast
    }

    /**
     * 用于测试...不使用
     */
    @Deprecated("")
    fun showBar(context: Context, content: String?) {
        val progressBar =
            ProgressBar(context.applicationContext, null, android.R.attr.progressBarStyleSmall)
        if (sToast == null) {
            sToast = Toast.makeText(context.applicationContext, content, Toast.LENGTH_SHORT)
        } else {
            sToast!!.view = progressBar
        }
        sToast!!.show()
    }
}