package com.tes_tix_id.android.configapp.exception

class ApiException(throwable: Throwable?, val code: Int) : RuntimeException(throwable) {
    @JvmField
    var msg: String? = null

}