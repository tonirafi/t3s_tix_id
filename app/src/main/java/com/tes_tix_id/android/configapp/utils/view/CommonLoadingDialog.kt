package com.tes_tix_id.android.configapp.utils.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tes_tix_id.android.R

class CommonLoadingDialog(private val mContext: Context) : Dialog(mContext, R.style.CommonDialog) {
    private var animation: AnimationDrawable? = null
    private var loading: ImageView? = null
    private var loadingText: TextView? = null
    private fun initWindow() {
        val window = this.window ?: return
        val params = window.attributes
        params.gravity = 17
        window.attributes = params
    }

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        initWindow()
        val view = this.layoutInflater.inflate(R.layout.layout_common_dialog, null)
        initView(view)
        this.setContentView(view)
    }

    private fun initView(view: View) {
        loading = view.findViewById(R.id.loading)
        loadingText = view.findViewById(R.id.loadingText)
        animation = loading!!.drawable as AnimationDrawable
    }

    override fun show() {
        if (mContext is Activity) {
            if (!mContext.isFinishing) {
                super.show()
                if (loading != null && animation != null) {
                    loading!!.clearAnimation()
                    animation!!.start()
                }
            }
        }
    }

    override fun dismiss() {
        if (this.isShowing) {
            if (animation != null) {
                animation!!.stop()
            }
            if (loading!!.animation != null) {
                loading!!.clearAnimation()
            }
            super.dismiss()
        }
    }

    fun show(text: String?) {
        this.show()
        loadingText!!.text = text
    }
}