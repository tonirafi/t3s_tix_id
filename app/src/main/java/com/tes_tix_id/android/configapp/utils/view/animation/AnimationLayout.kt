package com.tes_tix_id.android.configapp.utils.view.animation

import android.content.Context
import com.tes_tix_id.android.configapp.utils.base.IAnimationLayout
import butterknife.BindView
import com.tes_tix_id.android.R
import android.widget.TextView
import butterknife.OnClick
import android.view.ViewGroup
import io.reactivex.disposables.CompositeDisposable
import android.graphics.drawable.AnimationDrawable
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import io.reactivex.android.schedulers.AndroidSchedulers
import androidx.constraintlayout.widget.Guideline
import butterknife.ButterKnife
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class AnimationLayout(context: Context) : IAnimationLayout(context) {
    @JvmField
    @BindView(R.id.image_animation)
    var mAnimationView: ImageView? = null

    @JvmField
    @BindView(R.id.an_txt)
    var mAnTxt: TextView? = null

    @JvmField
    @BindView(R.id.btn_reload)
    var btnReload: Button? = null

    @JvmField
    @BindView(R.id.btn_back)
    var btnBack: Button? = null

    @JvmField
    @BindView(R.id.guideline)
    var guideline: Guideline? = null
    @OnClick(R.id.btn_back)
    fun back() {
        if (mListener != null) mListener!!.goBack()
    }

    @OnClick(R.id.btn_reload)
    fun reload() {
        if (isRunning || mListener == null) {
            return
        }
        if (exitWhenReload) {
            mListener!!.netErrorReload()
            (parent as ViewGroup).removeView(this)
        } else {
            startAnimation()
            mListener!!.netErrorReload()
        }
    }

    //    @OnClick(R.id.an_txt)
    //    void switchV() {
    //        netError(5000);
    //        updateActionButton(false);
    //    }
    val RUN_TEXT: String
    val RUN_TEXT_500: String
    val RUN_TEXT_200: String
    val RUN_TEXT_OTHER: String
    private var mListener: AnimationListener? = null
    private var isRunning = false
    private var showGoBackBtn = false
    private var showReloadBtn = false
    private var exitWhenReload = false
    private val autos = arrayOf("      ", " .    ", " . .  ", " . . .")
    private val mCompositeDisposable: CompositeDisposable
    private val mDrawable: AnimationDrawable? = null
    override fun setAnimationListener(mListener: AnimationListener) {
        this.mListener = mListener
    }

    override fun onDetachedFromWindow() {
        stopAnimation()
        super.onDetachedFromWindow()
    }

    override fun isRunning(): Boolean {
        return isRunning
    }

    override fun startAnimation() {
//        stopAnimation();
//        if (!(mAnimationView.getBackground() instanceof AnimationDrawable)) {
//            mAnTxt.setText(RUN_TEXT);
//            mAnimationView.setBackgroundResource(R.drawable.empty_logo);
//            hideActionButton();
//        }
//
//        if (getVisibility() != VISIBLE) {
//            setVisibility(VISIBLE);
//        }
//        isRunning = true;
//        mDrawable = (AnimationDrawable) mAnimationView.getBackground();
//        mDrawable.start();
        mCompositeDisposable.add(
                Observable.interval(0, 1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ aLong: Long -> mAnTxt!!.text = String.format("%s%s", RUN_TEXT, autos[(aLong % 4).toInt()]) }
                        ) { throwable: Throwable -> Log.e("AnimationLayout", String.format("[autoTxt]Error:%s", throwable.message)) }
        )
    }

    override fun stopAnimation() {
        isRunning = false
        if (mDrawable != null && mDrawable.isRunning) {
            mDrawable.stop()
        }
        mCompositeDisposable.clear()
    }

    override fun netError(code: Int) {
        if (mAnimationView == null) {
            return
        }
        stopAnimation()
        when (code) {
            500 -> {
                mAnTxt!!.text = RUN_TEXT_500
                //                mAnimationView.setBackgroundResource(R.drawable.net_error_500);
                updateActionButton()
            }
            200, 201 -> {
                mAnTxt!!.text = RUN_TEXT_200
                //                mAnimationView.setBackgroundResource(R.drawable.loading_failed);
                updateActionButton()
            }
            401 -> {
                if (mListener != null) {
                    mListener!!.authError()
                }
                mAnTxt!!.text = RUN_TEXT_OTHER
                //                mAnimationView.setBackgroundResource(R.drawable.loading_failed);
                updateActionButton()
            }
            403 -> {
                if (mListener != null) {
                    mListener!!.netForbiden()
                }
                mAnTxt!!.text = RUN_TEXT_OTHER
                updateActionButton()
            }
            else -> {
                mAnTxt!!.text = RUN_TEXT_OTHER
                updateActionButton()
            }
        }
    }

    var guidelineRatio: Float
        get() = (guideline!!.layoutParams as LayoutParams).guidePercent
        set(ratio) {
            guideline!!.setGuidelinePercent(ratio)
        }

    fun setActionButtonStatus(showGoBackBtn: Boolean, showReloadBtn: Boolean) {
        this.showGoBackBtn = showGoBackBtn
        this.showReloadBtn = showReloadBtn
    }

    fun updateActionButton() {
        btnBack!!.visibility = if (showGoBackBtn) VISIBLE else GONE
        btnReload!!.visibility = if (showReloadBtn) VISIBLE else GONE
    }

    fun hideActionButton() {
        btnBack!!.visibility = GONE
        btnReload!!.visibility = GONE
    }

    fun setExitWhenReload(exitWhenReload: Boolean) {
        this.exitWhenReload = exitWhenReload
    }

    class Builder : IAnimationLayout.Builder {
        private var showGoBackBtn = false
        private var showReloadBtn = true
        private var exitWhenReload = false
        private var guidelineRatio = 0.38f //0.55f
        fun showGoBackBtn(): Builder {
            showGoBackBtn = true
            return this
        }

        fun hideReloadBtn(): Builder {
            showReloadBtn = false
            return this
        }

        fun exitWhenReload(): Builder {
            exitWhenReload = true
            return this
        }

        fun guidelineRatio(ratio: Float): Builder {
            guidelineRatio = ratio
            return this
        }

        override fun build(context: Context): IAnimationLayout {
            val mAnimationLayout = AnimationLayout(context)
            mAnimationLayout.guidelineRatio = guidelineRatio
            mAnimationLayout.setExitWhenReload(exitWhenReload)
            mAnimationLayout.setActionButtonStatus(showGoBackBtn, showReloadBtn)
            return mAnimationLayout
        }
    }

    init {
        RUN_TEXT = context.getString(R.string.run_text)
        RUN_TEXT_500 = context.getString(R.string.run_text_500)
        RUN_TEXT_200 = context.getString(R.string.run_text_200)
        RUN_TEXT_OTHER = context.getString(R.string.run_text_other)
        //        setGravity(Gravity.CENTER);
//        setOrientation(VERTICAL);
        setBackgroundColor(resources.getColor(android.R.color.white))
        setPadding(paddingLeft, resources.getDimensionPixelSize(R.dimen.status_bar_height), paddingRight, paddingBottom)
        inflate(context, R.layout.layout_animation, this)
        ButterKnife.bind(this)
        mAnTxt!!.text = String.format("%s%s", RUN_TEXT, autos[0])
        mCompositeDisposable = CompositeDisposable()
    }
}