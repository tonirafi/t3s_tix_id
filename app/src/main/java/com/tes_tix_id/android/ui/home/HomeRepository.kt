package com.tes_tix_id.android.ui.home

import com.tes_tix_id.android.configapp.http.api.ApiService
import com.tes_tix_id.android.configapp.http.model.User
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent

class HomeRepository(val apiService: ApiService) : KoinComponent {

    fun loadDataUsers(limit:Int, page:Int): Observable<ArrayList<User>>? {
        return  apiService?.listUsers(limit,page).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    }
}