package com.tes_tix_id.android.configapp.exception

import android.net.ParseException
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.tes_tix_id.android.configapp.utils.bean.BaseResponse
import com.tes_tix_id.android.configapp.utils.bean.JsonBase
import me.samlss.broccoli.util.LogUtil
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException

object ApiExceptionHelper {
    fun getError(exception: HttpException): String? {
        val response = exception.response() ?: return exception.code().toString()
        val msg = response.message()
        val errorCode = response.code()
        if (errorCode != 404 && errorCode >= 400 && errorCode < 600) {
            if (response.errorBody() != null) {
                var errorInfo: String? = null
                try {
                    errorInfo = response.errorBody()!!.string()
                    if (!TextUtils.isEmpty(errorInfo)) {
                        val gson = Gson()
                        val jsonBase = gson.fromJson(errorInfo, JsonBase::class.java)
                        if (jsonBase != null) {
                            return jsonBase.msg
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                if (!TextUtils.isEmpty(errorInfo)) {
                    return errorInfo
                }
            }
            return if (TextUtils.isEmpty(msg)) errorCode.toString() else msg
        }
        return if (TextUtils.isEmpty(msg)) errorCode.toString() else String.format(
            "%s:%s",
            errorCode,
            msg
        )
    }

    fun getDisplayError(e: Throwable): String {
        return String.format("Oops,%s", handleException(e).msg)
    }

    fun getNetErrorCode(throwable: Throwable): Int {
        LogUtil.logE("getNetErrorCode: $throwable")
        return handleException(throwable).code
    }

    fun handleException(e: Throwable): ApiException {
        val ex: ApiException
        if (e is ApiException) {
            ex = e
        } else if (e is HttpException) {
            when {
                e.code() == ErrorCode.AUTH_ERROR -> {
                    ex = ApiException(e, ErrorCode.AUTH_ERROR)
                    val errorInfo = getError(e)
                    ex.msg = String.format("Auth error:%s", errorInfo ?: "401")
                }
                e.code() == ErrorCode.FORBIDEN -> {
                    ex = ApiException(e, ErrorCode.FORBIDEN)
                    val errorInfo = getError(e)
                    ex.msg = String.format("Forbiden error:%s", errorInfo ?: "403")
                }
                e.code() == 504 -> {
                    ex = ApiException(e, ErrorCode.NETWORK_ERROR)
                    val errorInfo = getError(e)
                    ex.msg = String.format("Network error:%s", errorInfo ?: "504")
                }
                else -> {
                    ex = ApiException(e, ErrorCode.HTTP_ERROR)
                    val errorInfo = getError(e)
                    ex.msg = errorInfo ?: "Http request error"
                }
            }
        } else if (e is ServerException) {
            val errorInfo = e.message
            if (e.code == ErrorCode.AUTH_ERROR) {
                ex = ApiException(e, ErrorCode.AUTH_ERROR)
                ex.msg = String.format("Auth error:%s", errorInfo)
            } else {
                ex = ApiException(e, ErrorCode.SERVER_ERROR)
                //                ex.msg = "Server error";
                ex.msg = if (TextUtils.isEmpty(errorInfo)) "Server error" else errorInfo
            }
        } else if (e is JsonParseException
            || e is JSONException
            || e is ParseException
        ) {
            ex = ApiException(e, ErrorCode.PARSE_ERROR)
            ex.msg = "Parse error" //均视为解析错误
        } else if (e is SocketTimeoutException) {
            ex = ApiException(e, ErrorCode.SERVER_TIME_OUT_ERROR)
            ex.msg = "Server response time out error" ////服务器响应超时
        } else if (e is ConnectException) {
            ex = ApiException(e, ErrorCode.NETWORK_ERROR)
            ex.msg = "Network connection error" //均视为网络连接错误
        } else {
            ex = ApiException(e, ErrorCode.UNKNOWN)
            ex.msg =
                if (TextUtils.isEmpty(e.localizedMessage)) "Unknown error" else e.localizedMessage
        }
        return ex
    }

    @Throws(ServerException::class)
    fun throwServerException(vararg its: BaseResponse<*>) {
        if (its == null || its.isEmpty()) return;
        for (it in its) {
            if (!it.isSuccess) {
                throw  ServerException(it.msg, it.code);
            }
        }

    }

    //    @Throws(ServerException::class)
//    fun throwAllServerException(vararg its: BaseResponse2<*>) throws ServerException {
//            if (its == null || its.length == 0) return;
//            throwServerException(its);
//            throwServerExceptionIfEmpty(its);
//        }
    @Throws(ServerException::class)
    fun throwServerExceptionIfEmpty(vararg its: BaseResponse<*>) {
        if (its == null || its.size == 0) return
        for (it in its) {
            if (!it.hasData()) {
                throw ServerException("Data is empty")
            }
        }
    }
}