/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tes_tix_id.android.configapp.utils.base

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tes_tix_id.android.configapp.utils.bean.Act
import com.tes_tix_id.android.configapp.utils.view.animation.AnimationListener

interface BaseView : AnimationListener {

    fun getContext(): Context?

//    fun getPresenter(): T?

    fun login(requestCode: Int = 1)

    fun bindMobile(userId: String?, openId: String?)

    fun setResult(resultCode: Int, data: Intent? = null)

    fun showLoading(
        cancelableOnBack: Boolean = true,
        cancelableOnTouch: Boolean = false,
        dialog: Dialog? = null
    )

    fun hideLoading()

    fun isAnimationLayoutShowing(): Boolean

    fun setAnimLayoutContainer(container: ViewGroup)

    fun showAnimation(guidelineRatio: Float)

    fun showAnimation(builder: IAnimationLayout.Builder? = null)

    fun hideAnimation()

    fun hideAnimationOrLoading(
        throwable: Throwable? = null,
        hintViewWhileError: Boolean = false,
        builder: IAnimationLayout.Builder? = null
    )

    fun netError(throwable: Throwable, builder: IAnimationLayout.Builder? = null)

    fun showMessage(@StringRes stringResId: Int)

    fun showMessage(throwable: Throwable)

    fun showMessage(message: String?)

    fun onComplete()

    fun parseIntent(intent: Intent?): Uri?

    fun popUpAct(act: Act)
}

abstract class IAnimationLayout(context: Context) : ConstraintLayout(context) {

    abstract fun startAnimation()

    abstract fun stopAnimation()

    abstract fun isRunning(): Boolean

    abstract fun netError(code: Int)

    abstract fun setAnimationListener(animationListener: AnimationListener)

    interface Builder {
        fun build(context: Context): IAnimationLayout
    }
}


