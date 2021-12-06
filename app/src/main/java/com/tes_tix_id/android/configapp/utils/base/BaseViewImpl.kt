package com.tes_tix_id.android.configapp.utils.base

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Looper
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.tes_tix_id.android.configapp.exception.ApiExceptionHelper
import com.tes_tix_id.android.configapp.utils.bean.Act
import com.tes_tix_id.android.configapp.utils.ToastUtil
import com.tes_tix_id.android.configapp.utils.view.CommonLoadingDialog
import com.tes_tix_id.android.configapp.utils.view.animation.AnimationLayout
import com.tes_tix_id.android.configapp.utils.view.animation.AnimationListener

class BaseViewImpl(
    private var root: ViewGroup?,
    private var animationListener: AnimationListener?
) : BaseView, ViewModel() {

    private var loadingDialog: Dialog? = null
    private var animLayout: IAnimationLayout? = null

    fun recycle() {
        root?.removeCallbacks(null)
        root = null

        animLayout?.stopAnimation()
        animLayout = null

        animationListener = null
    }

    override fun getContext(): Context? = root?.context


    override fun login(requestCode: Int) {
//        IntentUtil.intentToLogin(getContext(), requestCode)
    }

    override fun bindMobile(userId: String?, openId: String?) {
//        IntentUtil.intentToBindMobile(getContext(), openId, userId)
    }

    override fun setResult(resultCode: Int, data: Intent?) {

    }

    override fun showLoading(
        cancelableOnBack: Boolean,
        cancelableOnTouch: Boolean,
        dialog: Dialog?
    ) {
        loadingDialog = when {
            getContext() == null || loadingDialog?.isShowing == true -> return
            dialog != null -> dialog
            loadingDialog == null -> CommonLoadingDialog(getContext()!!)
            else -> loadingDialog
        }?.apply {
            setCanceledOnTouchOutside(cancelableOnTouch)
            setCancelable(cancelableOnBack)
            show()
        }
    }

    override fun hideLoading() {
        loadingDialog?.takeIf { it.isShowing }?.dismiss()
    }

    override fun isAnimationLayoutShowing(): Boolean {
        root?.let {
            val view = animLayout ?: return false
            return it.indexOfChild(view) != -1
        } ?: return false
    }

    override fun setAnimLayoutContainer(container: ViewGroup) {
        root?.let {
            hideAnimation()
        }
        this.root = container
    }

    override fun showAnimation(guidelineRatio: Float) {
        showAnimation(AnimationLayout.Builder().guidelineRatio(guidelineRatio))
    }

    override fun showAnimation(builder: IAnimationLayout.Builder?) {
        when {
            getContext() == null || isAnimationLayoutShowing() -> null
            builder != null -> builder.build(getContext()!!)
            animLayout != null -> animLayout
            else -> AnimationLayout.Builder().build(getContext()!!)
        }?.run {
            isClickable = true
            setAnimationListener(this@BaseViewImpl)
            root?.addView(this, 0, ViewGroup.LayoutParams(-1, -1))
            bringToFront()
            animLayout = this
            startAnimation()
        }
    }

    override fun hideAnimation() {
        animLayout?.let {
            it.stopAnimation()
            root?.removeView(it)
        }
    }

    override fun hideAnimationOrLoading(
        throwable: Throwable?,
        hintViewWhileError: Boolean,
        builder: IAnimationLayout.Builder?
    ) {
        if (throwable == null) {
            if (isAnimationLayoutShowing()) {
                hideAnimation()
            } else {
                hideLoading()
            }
        } else if (hintViewWhileError) {
            netError(throwable, builder)
        } else {
            showMessage(throwable)
        }
    }

    override fun netError(throwable: Throwable, builder: IAnimationLayout.Builder?) {
        showAnimation(builder)
        animLayout?.netError(ApiExceptionHelper.getNetErrorCode(throwable))
        showMessage(throwable)
    }

    override fun showMessage(stringResId: Int) {
        showMessage(getContext()?.getString(stringResId))
    }

    override fun showMessage(throwable: Throwable) {
        showMessage(ApiExceptionHelper.getDisplayError(throwable))
    }

    override fun showMessage(message: String?) {
        val context = getContext() ?: return
        message?.let {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                ToastUtil.show(context, it)
            } else {
                this.root?.post {
                    ToastUtil.show(context, it)
                }
            }
        }
    }

    override fun onComplete() {
        recycle()
    }

    override fun parseIntent(intent: Intent?): Uri? {
        return null
//        val extra = intent?.extras ?: return intent?.data
//
//        return extra.keySet()?.asSequence()?.find {
//            extra[it]?.toString()?.startsWith(RouterConstants.Common.SCHEME) ?: false
//        }?.let { Uri.parse(extra[it]?.toString()) } ?: intent?.data
    }

    override fun popUpAct(act: Act) {
        getContext()?.let {
            if (it is AppCompatActivity) it.supportFragmentManager else null
        }?.let {
            ActDialogFragment().show(it, act)
        }
    }

    override fun netErrorReload() {
        animationListener?.netErrorReload()
    }

    override fun netForbiden() {
        animationListener?.netErrorReload()
    }

    override fun goBack() {
        animationListener?.goBack()
    }

    override fun authError(): Boolean {
        return (animationListener?.authError() == true).takeUnless { it }?.also {
//            Passport.instance.signOut(false)
            login()
            !it
        } ?: true
    }


}