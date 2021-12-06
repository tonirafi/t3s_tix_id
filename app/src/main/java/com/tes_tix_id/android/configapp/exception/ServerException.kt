package com.tes_tix_id.android.configapp.exception

class ServerException : RuntimeException {
    var code: Int
        private set

    constructor(detailMessage: String?) : super(detailMessage) {
        code = 200
    }

    constructor(detailMessage: String?, code: Int) : super(detailMessage) {
        this.code = code
    }

}