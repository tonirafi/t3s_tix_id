package com.tes_tix_id.android.configapp.exception

interface ErrorCode {
    companion object {

        const val UNKNOWN = 1000


        const val PARSE_ERROR = 1001

        const val HTTP_ERROR = 1003


        const val NETWORK_ERROR = 500

        const val SERVER_ERROR = 200

        const val SERVER_TIME_OUT_ERROR = 201

        const val IllEGAL_USER_ERROR = 1004

        const val AUTH_ERROR = 401

        const val FORBIDEN = 403
    }
}