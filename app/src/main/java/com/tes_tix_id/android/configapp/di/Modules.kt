package com.tes_tix_id.android.configapp.di

import com.google.gson.GsonBuilder
import com.tes_tix_id.android.BuildConfig
import com.tes_tix_id.android.configapp.http.api.ApiService
import com.tes_tix_id.android.ui.home.HomeRepository
import com.tes_tix_id.android.ui.home.HomeViewModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val appModule = module {
    factory { restAdapter() }
    single { HomeRepository(get()) }
    viewModel { HomeViewModel(get()) }
}


fun restAdapter(): ApiService? {
    val retrofit: Retrofit
    try {

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        var onlineInterceptor = Interceptor { chain ->
            val response: Response = chain.proceed(chain.request())
            val maxAge =
                60 // read from cache for 60 seconds even if there is internet connection
            response.newBuilder()
                .header("Cache-Control", "public, max-age=$maxAge")
                .removeHeader("Pragma")
                .build()
        }

        var offlineInterceptor = Interceptor { chain ->
            var request: Request = chain.request()
//                if (!isNetworkAvailable(getContext())) {
//                    val maxStale = 60 * 60 * 24 * 30 // Offline cache available for 30 days
//                    request = request.newBuilder()
//                            .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
//                            .removeHeader("Pragma")
//                            .build()
//                }
            chain.proceed(request)
        }

        val gson = GsonBuilder()
            .create()


        var cacheSize = 10 * 1024 * 1024 // 10 MB

//            var cache = Cache(getContext().cacheDir, cacheSize.toLong())

        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.MINUTES)
            .readTimeout(10, TimeUnit.MINUTES)
            .addInterceptor(offlineInterceptor)
            .addInterceptor(logging)
            .addNetworkInterceptor(onlineInterceptor)
            .connectTimeout(90.toLong(), TimeUnit.SECONDS)
            .readTimeout(90.toLong(), TimeUnit.SECONDS)
            .writeTimeout(90.toLong(), TimeUnit.SECONDS)
//                .cache(cache)
            .build()



        retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.URL_DOMAIN)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }


}









