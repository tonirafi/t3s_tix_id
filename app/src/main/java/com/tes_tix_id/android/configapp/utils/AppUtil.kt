package com.tes_tix_id.android.configapp.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.util.SparseArray
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.FontRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tes_tix_id.android.MyApplication
import com.tes_tix_id.android.R

object AppUtil {


    fun recycleBackground(view: View?) {
        if (view != null && view.background != null) {
            val bd: BitmapDrawable = view.background as BitmapDrawable
            recycleBitmapDrawable(bd)
            view.setBackgroundResource(0)
        }
    }

    fun recycleImageView(imageView: ImageView?) {
        if (imageView != null) {
            if (imageView.drawable != null) { //释放图片资源
                recycleBitmapDrawable(imageView.drawable)
                imageView.setImageDrawable(null)
            }
            //释放背景资源
            recycleBackground(imageView)
        }
    }

    fun recycleBitmapDrawable(drawable: Drawable?) {
        if (drawable != null) {
            if (drawable is BitmapDrawable) {
                recycleBitmap((drawable as BitmapDrawable).getBitmap())
            }
            drawable.callback = null
        }
    }

    fun recycleBitmap(bitmap: Bitmap?) {
        if (bitmap != null && !bitmap.isRecycled()) bitmap.recycle()
    }

    fun getAppH5CachePath(context: Context): String {
        return context.applicationContext.filesDir.absolutePath + "H5Cache/"
    }

    private fun getPackageInfo(context: Context): PackageInfo? {
        var pi: PackageInfo? = null
        try {
            val pm: PackageManager = context.packageManager
            pi = pm.getPackageInfo(context.packageName, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return pi
    }

    fun getAppVersionCode(context: Context): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getPackageInfo(context)!!.longVersionCode
                .toInt()
        } else getPackageInfo(context)!!.versionCode
    }

    fun getAppVersionName(context: Context): String {
        return getPackageInfo(context)!!.versionName
    }

    /**
     * 是否是系统预装app
     *
     *
     * (flags & ApplicationInfo.FLAG_SYSTEM) != 0 表示系统程序
     * (flags & ApplicationInfo.FLAG_SYSTEM) <= 0  表示第三方应用程序
     * (flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0  表示系统程序被手动更新后，也成为第三方应用程序
     *
     * @param context
     * @return
     */
    fun isPreInstalledApp(context: Context): Boolean {
        val flags = getPackageInfo(context)!!.applicationInfo.flags
        return flags and ApplicationInfo.FLAG_SYSTEM != 0
    }

    fun getAppMetaData(context: Context, key: String?): String? {
        try {
            val pm: PackageManager = context.packageManager
            val applicationInfo: ApplicationInfo = pm.getApplicationInfo(
                context.packageName,
                PackageManager.GET_META_DATA
            )
            return applicationInfo.metaData.getString(key)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    //    public static boolean isGooglePlayServiceAvailable(Context context) {
    //        int status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
    //        if (status == ConnectionResult.SUCCESS) {
    //            com.qschou.pedulisehat.android.utils.LogUtil.logE("GooglePlayService", "GooglePlayServicesUtil service is available.");
    //            return true;
    //        }
    //
    //        com.qschou.pedulisehat.android.utils.LogUtil.logE("GooglePlayService", "GooglePlayServicesUtil service is NOT available.");
    //        return false;
    //    }
    //
    //
    //    /**
    //     * Check the device to make sure it has the Google Play Services APK. If
    //     * it doesn't, display a dialog that allows users to download the APK from
    //     * the Google Play Store or enable it in the device's system settings.
    //     */
    //    public static boolean checkIsGooglePlayServiceAvailable(AppCompatActivity context) {
    ////        int statusCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
    ////        if (statusCode != ConnectionResult.SUCCESS) {
    ////            GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(context);
    ////        }
    //
    //        if (BuildConfig.DEBUG) return true;
    //
    //        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
    //        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
    //        if (resultCode != ConnectionResult.SUCCESS) {
    //            if (apiAvailability.isUserResolvableError(resultCode)) {
    //                apiAvailability.getErrorDialog(context, resultCode, AppConstants.RequestCode.RSC_PLAY_SERVICES_RESOLUTION_REQUEST
    //                        , dialog -> {
    ////                            dialog.dismiss();
    //                            context.finish();
    //                        }).show();
    //            } else {
    //                LogUtil.logI("Google play service is not supported on this device .");
    //                context.finish();
    //            }
    //            return false;
    //        }
    //        return true;
    //    }
    fun getStatusBarHeight(context: Context): Int {
        var statusBarHeight = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = context.resources.getDimensionPixelSize(resourceId)
        }
        return statusBarHeight
    }

    fun insertStatusBarHeight2TopPadding(view: View) {
        setTopPadding(view, view.paddingTop + getStatusBarHeight(view.context))
    }

    fun setBottomPadding(view: View, bottomPadding: Int) {
        view.setPadding(view.paddingLeft, view.paddingTop, view.paddingRight, bottomPadding)
    }

    fun setTopPadding(view: View, topPadding: Int) {
        view.setPadding(view.paddingLeft, topPadding, view.paddingRight, view.paddingBottom)
    }

    fun <T> getFieldInstance(`object`: Any?, fieldName: String?, clz: Class<T>): T? {
        if (`object` == null) return null
        try {
            val declaredField = `object`.javaClass.getDeclaredField(fieldName!!)
            if (!declaredField.isAccessible) {
                declaredField.isAccessible = true
            }
            val field = declaredField[`object`]
            if (clz.isInstance(field)) {
                return clz.cast(field)
            }
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        return null
    }

    //    public static void fixFacebookLeak() {
    //        try {
    //
    //            Field f = ActivityLifecycleTracker.class.getDeclaredField("viewIndexer");
    //            if (!f.isAccessible()) {
    //                f.setAccessible(true);
    //            }
    //
    //            Object fieldVal = f.get(null);
    //            if (fieldVal instanceof ViewIndexer) {
    //                ((ViewIndexer) fieldVal).unschedule();
    //                Timer timer = getFieldInstance(fieldVal, "indexingTimer", Timer.class);
    //                if (timer != null) {
    //                    timer.cancel();
    //                }
    //
    //                Handler handler = getFieldInstance(fieldVal, "uiThreadHandler", Handler.class);
    //                if (handler != null) {
    //                    handler.removeCallbacksAndMessages(null);
    //                }
    //            }
    //
    //        } catch (NoSuchFieldException e) {
    //            e.printStackTrace();
    //        } catch (IllegalAccessException e) {
    //            e.printStackTrace();
    //        }
    //    }
    fun fixInputMethodManagerLeak(context: Context?) {
        if (context == null) return
        var inputMethodManager: InputMethodManager? = null
        try {
            inputMethodManager =
                context.applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        } catch (th: Throwable) {
            th.printStackTrace()
        }
        if (inputMethodManager == null) return
        val strArr = arrayOf("mCurRootView", "mServedView", "mNextServedView")
        for (i in 0..2) {
            try {
                val declaredField = inputMethodManager.javaClass.getDeclaredField(strArr[i])
                    ?: continue
                if (!declaredField.isAccessible) {
                    declaredField.isAccessible = true
                }
                val obj = declaredField[inputMethodManager]
                if (obj == null || obj !is View) continue
                if (obj.context === context) {
                    declaredField[inputMethodManager] = null
                } else {
                    return
                }
            } catch (th: Throwable) {
                th.printStackTrace()
            }
        }
    }

    private const val UNREAD_MSG_COUNT = "UnreadNotificationCount"
    private const val DONOR_VERSION = "donorVersion"

    //    public static void removeUser(String userId) {
    //        if (userId == null) return;
    //        final String key = String.format("user[%s]", userId);
    //        SharedPreferencesTool.getInstance(MyApplication.getContext()).putString(key, null);
    //    }
    //
//    fun saveUser(user: User?) {
//        if (user == null) return
//        val key = String.format("user[%s]", user.userId)
//        com.tes_tix_id.android.configapp.utils.SharedPreferencesTool.getInstance(MyApplication.getContext())
//            ?.putObject(key, user)
//    }

//    //
//    fun getUser(userId: String?): User? {
//        if (userId == null) return null
//        val key = String.format("user[%s]", userId)
//        return SharedPreferencesTool.getInstance(MyApplication.getContext())
//            ?.getObject(key, User::class.java, null)
//    }

    //
    fun getUnreadNotificationCount(user_id: String): Int {
        return if (TextUtils.isEmpty(user_id)) 0 else com.tes_tix_id.android.configapp.utils.SharedPreferencesTool.getInstance(
            MyApplication.getContext()
        )!!.getInt(UNREAD_MSG_COUNT + user_id, 0)
    }

    //
    fun updateUnreadNotificationCount(user_id: String, count: Int) {
        if (TextUtils.isEmpty(user_id)) return
        com.tes_tix_id.android.configapp.utils.SharedPreferencesTool.getInstance(MyApplication.getContext())
            ?.putInt(
                UNREAD_MSG_COUNT + user_id, count
            )
    }

    //
    fun getDonorVersionVal(user_id: String): Boolean {
        return if (TextUtils.isEmpty(user_id)) true else SharedPreferencesTool.getInstance(
            MyApplication.getContext()
        )!!.getBoolean(DONOR_VERSION + user_id, true)
    }

    //
    fun saveDonorVersionVal(user_id: String, donorVersion: Boolean) {
        if (TextUtils.isEmpty(user_id)) return
        SharedPreferencesTool.getInstance(MyApplication.getContext())
            ?.putBoolean(
                DONOR_VERSION + user_id, donorVersion
            )
    }

    //
    //
    fun tintDrawable(drawable: Drawable?, @ColorInt color: Int): Drawable? {
        if (drawable == null) return null
        val wrappedDrawable: Drawable = DrawableCompat.wrap(drawable).mutate()
        DrawableCompat.setTint(wrappedDrawable, color)
        return wrappedDrawable
    }

    //
    private val fontCache: SparseArray<Typeface> = SparseArray<Typeface>(2)
    fun getFont(context: Context, @FontRes id: Int): Typeface? {
        if (id == -1) { //id=-1 do preload and cache font resource
            getFont(context, R.font.helvetica)
            getFont(context, R.font.helvetica)
            getFont(context, R.font.helvetica)
            return null
        }
        val typeface: Typeface = fontCache.get(id)
        if (typeface != null) {
            return typeface
        }
        try {
            ResourcesCompat.getFont(context.applicationContext, id, object : ResourcesCompat.FontCallback() {
                override fun onFontRetrieved(typeface: Typeface) {
                    fontCache.append(id, typeface)
                }

                override fun onFontRetrievalFailed(reason: Int) {}
            }, null)
            //        } catch (Resources.NotFoundException e) {
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return if (id != R.font.helvetica) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
    }

    fun isRouterUriMatch(a: Uri?, b: Uri?): Boolean {
        if (a == null || b == null) return false
        if (a != b) {
            if (a.scheme == null || a.scheme != b.scheme) return false
            if (a.authority == null || a.authority != b.authority) return false
            return if (TextUtils.isEmpty(a.path)) {
                TextUtils.isEmpty(b.path)
            } else a.path == b.path
        }
        return true
    }

    val gsonInstance: Gson
        get() = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create()

    fun ConverterJsonToObject(): Gson {
        return GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create()
    }
}