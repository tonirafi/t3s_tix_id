package com.tes_tix_id.android.configapp.http.api

import com.tes_tix_id.android.BuildConfig
import com.tes_tix_id.android.configapp.http.model.User
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

    @Headers("Content-Type: application/json")
    @GET(BuildConfig.URL_DOMAIN + "users")
    fun listUsers(
        @Query("per_page") limit: Int,
        @Query("since") page: Int
    ): Observable<ArrayList<User>>

}