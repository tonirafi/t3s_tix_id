package com.tes_tix_id.android.configapp.utils.bean

open class BaseResponse<T> {
    var code = 0
    var msg: String? = null
    var data: T? = null
        private set
    val isSuccess: Boolean
        get() = code == 0

    val isResponseSuccessful: Boolean
        get() = isSuccess && hasData()

    fun hasData(): Boolean {
        return data != null
    }

    val localMsg: String?
        get() = if (!hasData()) "the data in BaseResponse is null" else msg

    fun setData(data: T) {
        this.data = data
    }
}