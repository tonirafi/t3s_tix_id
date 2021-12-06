package com.tes_tix_id.android.configapp.utils

import android.net.Uri
import android.text.TextUtils
import androidx.annotation.DrawableRes
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import com.tes_tix_id.android.R

object PicassoUtil {
    private const val TAG = "PicassoTag"
    fun loadRaw(@DrawableRes resourceId: Int): RequestCreator {
        return Picasso.get().load(resourceId)
    }

    fun loadRaw(path: String?): RequestCreator {
        if (TextUtils.isEmpty(path)) {
            return Picasso.get().load(R.drawable.empty_logo)
        }
        val uri = Uri.parse(path)
        //本地资源 通过res:///resId 引用
        if ("res" == uri.scheme && !TextUtils.isEmpty(uri.lastPathSegment)) {
            try {
                val resId = uri.lastPathSegment!!.toInt()
                return Picasso.get().load(resId)
            } catch (e: NumberFormatException) {
            }
        }
        return Picasso.get().load(path).tag(TAG)
    }


//    fun load(path: Any): RequestCreator? {
//        return loadIfDrawableRes(path, load(path.toString()))
//    }
//
//    fun load(path: Any, rectangular: Boolean): RequestCreator? {
//        return loadIfDrawableRes(path, load(path.toString(), rectangular))
//    }
//
//    fun loadAvatar(path: Any): RequestCreator? {
//        return loadIfDrawableRes(path, loadAvatar(path.toString()))
//    }

    @JvmOverloads
    fun load(path: String?, rectangular: Boolean = false): RequestCreator {
        val resId =
            if (rectangular) R.drawable.ll_divider_gray_1dp else R.drawable.ll_divider_gray_1dp
        return load(path, resId, resId)
    }

    fun loadAvatar(path: String?): RequestCreator {
        return load(
            path,
            R.drawable.ll_divider_gray_1dp,
            R.drawable.ll_divider_gray_1dp
        ).centerCrop().fit()
    }

    fun load(
        path: String?,
        @DrawableRes placeholderResId: Int,
        @DrawableRes errorResId: Int
    ): RequestCreator {
        return loadRaw(if (TextUtils.isEmpty(path)) "res:///$placeholderResId" else path)
            .placeholder(placeholderResId)
            .error(errorResId)
    }

    private fun loadIfDrawableRes(imageUrl: Any, requestCreator: RequestCreator): RequestCreator {
        return if (imageUrl is Int) {
            loadRaw(imageUrl.toString().toInt())
        } else requestCreator
    }

    @JvmOverloads
    fun pauseTag(tag: Any = TAG) {
        Picasso.get().pauseTag(tag)
    }

    @JvmOverloads
    fun resumeTag(tag: Any = TAG) {
        Picasso.get().resumeTag(tag)
    }
}