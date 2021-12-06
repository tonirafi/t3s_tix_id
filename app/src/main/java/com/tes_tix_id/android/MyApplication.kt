package com.tes_tix_id.android

import androidx.multidex.MultiDexApplication
import com.tes_tix_id.android.configapp.di.appModule
import io.reactivex.plugins.RxJavaPlugins
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import java.util.*


open class MyApplication : MultiDexApplication() {



    override fun onCreate() {
        super.onCreate()
        mInstance = this

        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(appModule)
        }
        RxJavaPlugins.setErrorHandler {
            it.printStackTrace()
        }

    }




    companion object {
        private const val TAG = "MyApplication"
        private lateinit var mInstance: MyApplication

        @JvmStatic
        fun getContext(): MyApplication {
            return mInstance
        }

    }

}