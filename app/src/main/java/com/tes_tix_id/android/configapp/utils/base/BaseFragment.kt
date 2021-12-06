package com.tes_tix_id.android.configapp.utils.base

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tes_tix_id.android.configapp.utils.bean.Act

abstract class BaseFragment : Fragment(), BaseView {

    private var viewHasInstantiated: Boolean = false
    private var firstLoadData = true


    private lateinit var baseView: BaseViewImpl

    private var savedInstanceState: Bundle? = null

    protected fun hasSavedInstanceState() = this.savedInstanceState != null

    override fun onCreate(savedInstanceState: Bundle?) {
        this.savedInstanceState =
            savedInstanceState  //必须放在 super.onCreate()方法调用前 不然onAttachFragment中的判断会失效
        super.onCreate(savedInstanceState)
//        userVisibleHint = savedInstanceState?.getBoolean("userVisibleHint") ?: userVisibleHint
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (this::baseView.isInitialized) {
            this.baseView.recycle()
        }

        val rootView = view as? ViewGroup
            ?: throw IllegalArgumentException("BaseFragment baseView initial fail.")
        baseView = BaseViewImpl(rootView, this)
    }

//    override fun preparePresenter(): T? = null

    override fun setAnimLayoutContainer(container: ViewGroup) {
        baseView.setAnimLayoutContainer(container)
    }

    open fun onBackPressed(): Boolean {
        childFragmentManager.fragments.forEach {
            if (it is BaseFragment) {
                if (it.isVisible && it.onBackPressed()) return true
            }

            if (it is BaseDialogFragment) {
                if (it.isVisible && it.dialog?.isShowing == true && it.onBackPressed()) return true
            }
        }
        return false
    }


    override fun onResume() {
        super.onResume()
    }


    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        userVisibleHint = !hidden
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            onUserVisible(this.savedInstanceState)
        }
    }

    private fun onUserVisible(savedInstanceState: Bundle?) {
        if (viewHasInstantiated && firstLoadData) {
            firstLoadData = false
            onLazyLoadData(savedInstanceState)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewHasInstantiated = true
        if (userVisibleHint) {
            onUserVisible(savedInstanceState)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        baseView.recycle()
    }

    override fun onDestroy() {
        baseView.recycle()
        super.onDestroy()
//        MyApplication.getContext().refWatcher.watch(this)
    }

    abstract fun onLazyLoadData(savedInstanceState: Bundle?)

    override fun onStop() {
        super.onStop()
        hideLoading()
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putBoolean("userVisibleHint", false)
//    }

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

    override fun onComplete() {
        baseView.recycle()
        activity?.finish()
    }

    override fun bindMobile(userId: String?, openId: String?) {
        baseView.bindMobile(userId, openId)
    }

    override fun login(requestCode: Int) {
        baseView.login(requestCode)
    }

    override fun setResult(resultCode: Int, data: Intent?) {
        activity?.setResult(resultCode, data)
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