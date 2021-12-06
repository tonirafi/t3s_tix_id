package com.tes_tix_id.android.configapp.utils.base

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tes_tix_id.android.R
import com.tes_tix_id.android.configapp.utils.bean.Act


abstract class BaseDialogFragment : AppCompatDialogFragment(), BaseView {

    override fun onDetach() {
        super.onDetach()
        context?.let {
            if (it is DialogInterface.OnDismissListener) {
                it.onDismiss(null)
            }
        }
    }

    open fun bottomSheetMode(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!bottomSheetMode()) {
            setStyle(STYLE_NO_TITLE, R.style.DialogFragmentStyle)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return if (bottomSheetMode()) BottomSheetDialog(
            requireContext(),
            theme
        ) else super.onCreateDialog(savedInstanceState)
    }

    open fun showNow(manager: FragmentManager) {
        super.showNow(manager, this.javaClass.simpleName)
    }

    open fun show(manager: FragmentManager) {
//        super.show(manager, this.javaClass.simpleName)
        val ft = manager.beginTransaction()
        ft.add(this, tag)
        ft.commitAllowingStateLoss()
    }

    override fun onStart() {
        super.onStart()
        if (bottomSheetMode()) {
            val bottomSheet = dialog?.findViewById<View>(R.id.design_bottom_sheet)
//            bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            bottomSheet?.setBackgroundResource(android.R.color.transparent)
        } else {
            dialog?.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    open fun onBackPressed(): Boolean {
        return false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (this::baseView.isInitialized) {
            this.baseView.recycle()
        }

        val rootView = view as? ViewGroup
            ?: throw IllegalArgumentException("BaseDialogFragment baseView initial fail.")
        baseView = BaseViewImpl(rootView, this)
    }

//    override fun preparePresenter(): T? = null

    override fun setAnimLayoutContainer(container: ViewGroup) {
        baseView.setAnimLayoutContainer(container)
    }

//    final override fun getPresenter(): T? {
//        return baseView.getPresenter()
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        baseView.recycle()
    }

    override fun onDestroy() {
        baseView.recycle()
        super.onDestroy()
//        MyApplication.getContext().refWatcher.watch(this)
    }

    override fun dismiss() {
        super.dismiss()
        baseView.recycle()
    }

    private lateinit var baseView: BaseViewImpl

    override fun onStop() {
        super.onStop()
        hideLoading()
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

    override fun onComplete() {
        dismiss()
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

    override fun authError(): Boolean {
        onComplete()
        return false
    }

    override fun netForbiden() {

    }
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