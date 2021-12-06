package com.tes_tix_id.android.configapp.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import java.util.*

@SuppressLint("ViewConstructor")
class KeyboardHelper private constructor(activity: Activity) : FrameLayout(activity) {
    private val statusBarHeight: Int
    private val navigationBaHeight: Int
    private var viewInset = -1
    var keyboardHeight = 0
        private set

    //    private List<WeakReference<KeyboardHelper.Listener>> keyboardListener = new ArrayList<>();
    private val keyboardListener: MutableList<Listener> = ArrayList()
    private var keyboardSizeListener: SizeListener? = null
    val inputTrap: EditText
    private fun getKeyboardHeight(activity: Activity): Int {
        val r = Rect()
        val view = activity.window.decorView
        view.getWindowVisibleDisplayFrame(r)
        val keyboardHeight = viewPortHeight - (r.bottom - r.top)
        return if (keyboardHeight < 0) 0 else keyboardHeight
    }

    private val viewPortHeight: Int
        private get() = rootView.height - statusBarHeight - cachedInset

    /**
     * 顶部状态栏高度
     *
     * @return
     */
    private fun getStatusBarHeight(): Int {
        val statusBarRes = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (statusBarRes > 0) resources.getDimensionPixelSize(statusBarRes) else 0
    }

    /**
     * 底部底部导航栏高度
     *
     * @return
     */
    private val navigationBarHeight: Int
        private get() {
            val navigationBarRes =
                resources.getIdentifier("navigation_bar_height", "dimen", "android")
            return if (navigationBarRes > 0) resources.getDimensionPixelSize(navigationBarRes) else 0
        }
    private val cachedInset: Int
        private get() {
            if (viewInset == -1) {
                viewInset = getViewInset()
            }
            return viewInset
        }

    private fun getViewInset(): Int {
        var height = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //当项目targetSdkVersion>28时  代码在Android 10 API29 及以上设备运行时时 无法获取该数据
            //将项目targetSdkVersion设置为28及以下时  可规避该问题
            try {
                val attachInfoField = View::class.java.getDeclaredField("mAttachInfo")
                attachInfoField.isAccessible = true
                val attachInfo = attachInfoField[this]
                if (attachInfo != null) {
                    val stableInsetsField = attachInfo.javaClass.getDeclaredField("mStableInsets")
                    stableInsetsField.isAccessible = true
                    val insets = stableInsetsField[attachInfo] as Rect
                    height = insets.bottom
                    //                    return insets.bottom;
                }
            } catch (e: Exception) {
                // well .... at least we tried
                height = navigationBaHeight
            }
        }
        return height
    }

    val isKeyboardVisible: Boolean
        get() = keyboardHeight > 0

    fun addListener(keyboardListener: Listener) {
//        this.keyboardListener.add(new WeakReference<>(keyboardListener));
        this.keyboardListener.add(keyboardListener)
    }

    fun setKeyboardHeightListener(sizeListener: SizeListener?) {
        keyboardSizeListener = sizeListener
    }

    fun clearAllListeners() {
        keyboardListener.clear()
        keyboardSizeListener = null
    }

    private inner class KeyboardTreeObserver(private val activity: Activity) :
        OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            val keyboardHeight = getKeyboardHeight(activity)
            if (!keyboardListener.isEmpty()) {
                if (keyboardHeight > 0) {
                    if (this@KeyboardHelper.keyboardHeight == 0) {
                        notifyKeyboardVisible()
                    }
                } else {
                    if (this@KeyboardHelper.keyboardHeight > 0) {
                        notifyKeyboardDismissed()
                    }
                }
            }

//            if (keyboardSizeListener != null && KeyboardHelper.this.keyboardHeight != keyboardHeight) {
//                keyboardSizeListener.onSizeChanged(keyboardHeight);
//            }
            if (this@KeyboardHelper.keyboardHeight != keyboardHeight) {
                this@KeyboardHelper.keyboardHeight = keyboardHeight
                if (keyboardSizeListener != null) {
                    keyboardSizeListener!!.onSizeChanged(keyboardHeight)
                }
            }
        }
    }

    private fun notifyKeyboardVisible() {
//        for (WeakReference<KeyboardHelper.Listener> listeners : keyboardListener) {
//            if (listeners.get() != null) {
//                listeners.get().onKeyboardVisible();
//            }
//        }
        for (li in keyboardListener) {
            li?.onKeyboardVisible()
        }
    }

    private fun notifyKeyboardDismissed() {
//        for (WeakReference<KeyboardHelper.Listener> listeners : keyboardListener) {
//            if (listeners.get() != null) {
//                listeners.get().onKeyboardDismissed();
//            }
//        }
        for (li in keyboardListener) {
            li?.onKeyboardDismissed()
        }
    }

    interface Listener {
        fun onKeyboardVisible()
        fun onKeyboardDismissed()
    }

    interface SizeListener {
        fun onSizeChanged(keyboardHeight: Int)
    }

    companion object {
        fun inject(activity: Activity): KeyboardHelper {
            val decorView = activity.window.decorView as ViewGroup
            var i = 0
            val c = decorView.childCount
            while (i < c) {
                if (decorView.getChildAt(i) is KeyboardHelper) {
                    return decorView.getChildAt(i) as KeyboardHelper
                }
                i++
            }
            val keyboardHelper = KeyboardHelper(activity)
            decorView.addView(keyboardHelper)
            return keyboardHelper
        }

        fun showKeyboard(editText: EditText) {
            editText.post {
                if (editText.requestFocus()) {
                    val imm =
                        editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm?.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
                }
            }
        }

        fun hideKeyboard(activity: Activity) {
            val view = activity.currentFocus
            if (view != null) {
                val imm =
                    view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

    init {
        statusBarHeight = getStatusBarHeight()
        navigationBaHeight = navigationBarHeight
        layoutParams = ViewGroup.LayoutParams(0, 0)
        inputTrap = EditText(activity)
        inputTrap.isFocusable = true
        inputTrap.isFocusableInTouchMode = true
        inputTrap.visibility = VISIBLE
        inputTrap.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
        inputTrap.inputType = EditorInfo.TYPE_TEXT_FLAG_CAP_SENTENCES
        addView(inputTrap)
        val rootView = activity.window.decorView.findViewById<View>(Window.ID_ANDROID_CONTENT)
        rootView.viewTreeObserver.addOnGlobalLayoutListener(KeyboardTreeObserver(activity))
    }
}