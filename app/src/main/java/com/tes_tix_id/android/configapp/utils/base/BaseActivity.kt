package com.tes_tix_id.android.configapp.utils.base

import android.R
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.tes_tix_id.android.configapp.utils.bean.Act
import com.tes_tix_id.android.configapp.utils.LocaleUtil
import com.tes_tix_id.android.configapp.utils.StatusBarUtil


abstract class BaseActivity : AppCompatActivity(), BaseView {

    private lateinit var baseView: BaseViewImpl

    private val receiver by lazy {
//        BaseActBroadcastReceiver(object : BaseActBroadcastReceiver.EventListener {
//            override fun localeChanged() {
//                recreate()
//            }
//
//            override fun finishActivity() {
//                finish()
//            }
//        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (this::baseView.isInitialized) {
            this.baseView.recycle()
        }
        baseView = BaseViewImpl(window.decorView.findViewById(R.id.content), this)
        if (needRegisterReceiver()) {
//            LocalBroadcastManager.getInstance(this).registerReceiver(receiver, receiver.filter)
        }
    }

    override fun setAnimLayoutContainer(container: ViewGroup) {
        baseView.setAnimLayoutContainer(container)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBar()
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleUtil.onAttach(newBase!!))
    }

    open fun needRegisterReceiver(): Boolean = false

    open fun unregisterReceiver() {
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
//        receiver.removeEventListener()
    }

    protected open fun setStatusBar() {
        StatusBarUtil.setLightMode(this)
    }

    override fun getContext(): Context {
        return this
    }


    override fun onBackPressed() {
        if (fragmentsOnBackPressed())
            return
        super.onBackPressed()
    }

    protected fun fragmentsOnBackPressed(): Boolean {
        supportFragmentManager.fragments.forEach {
            if (it is BaseFragment) {
                if (it.isVisible && it.onBackPressed()) return true
            }

            if (it is BaseDialogFragment) {
                if (it.isVisible && it.dialog?.isShowing == true && it.onBackPressed()) return true
            }
        }

        return false
    }

    override fun onDestroy() {
        baseView.recycle()
        super.onDestroy()
        if (needRegisterReceiver()) {
            unregisterReceiver()
        }
        //修复输入法引起的泄露
//        AppUtil.fixInputMethodManagerLeak(this)
    }

    override fun onComplete() {
        baseView.recycle()
        finish()
    }

    override fun showLoading(
        cancelableOnBack: Boolean,
        cancelableOnTouch: Boolean,
        dialog: Dialog?
    ) {
        baseView.showLoading(cancelableOnBack, cancelableOnTouch, dialog)
    }

    override fun hideLoading() {
        baseView.hideLoading()
    }

    override fun showMessage(stringResId: Int) {
        baseView.showMessage(stringResId)
    }

    override fun showMessage(throwable: Throwable) {
        baseView.showMessage(throwable)
    }

    override fun showMessage(message: String?) {
        baseView.showMessage(message)
    }

    override fun bindMobile(userId: String?, openId: String?) {
        baseView.bindMobile(userId, openId)
    }

    override fun login(requestCode: Int) {
        baseView.login(requestCode)
    }

    override fun showAnimation(guidelineRatio: Float) {
        baseView.showAnimation(guidelineRatio)
    }

    override fun showAnimation(builder: IAnimationLayout.Builder?) {
        baseView.showAnimation(builder)
    }

    override fun isAnimationLayoutShowing(): Boolean = baseView.isAnimationLayoutShowing()

    override fun hideAnimation() {
        baseView.hideAnimation()
    }

    override fun netError(throwable: Throwable, builder: IAnimationLayout.Builder?) {
        baseView.netError(throwable, builder)
    }

    override fun netErrorReload() {
    }

    override fun goBack() {
        onComplete()
    }

    override fun authError(): Boolean = false

    override fun parseIntent(intent: Intent?): Uri? = baseView.parseIntent(intent)

    override fun hideAnimationOrLoading(
        throwable: Throwable?,
        hintViewWhileError: Boolean,
        builder: IAnimationLayout.Builder?
    ) {
        baseView.hideAnimationOrLoading(throwable, hintViewWhileError, builder)
    }

    override fun popUpAct(act: Act) {
        baseView.popUpAct(act)
    }
}