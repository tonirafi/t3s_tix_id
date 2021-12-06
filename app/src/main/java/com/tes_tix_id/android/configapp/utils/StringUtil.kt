package com.tes_tix_id.android.configapp.utils

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.tes_tix_id.android.configapp.utils.LocaleUtil.getSystemLocale
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

object StringUtil {
    fun isMobileNO(mobiles: String?): Boolean {
        //手机
        var IS_MOBEILPHONE = false
        //        String phoneExp= "(^(0\\d{2,3})?-?([2-9]\\d{6,7})(-\\d{1,5})?$)|(^((\\(\\d{3}\\))|(\\d{0}))?(13|14|15|17|18)\\d{9}$)|(^(400|800)\\d{7}(-\\d{1,6})?$)|(^(95013)\\d{6,8}$)";
        val phoneExp =
            "(^((0\\d{2,3})-)?([2-9]\\d{6,7})(-\\d{1,5})?$)|(^(086)?(13|14|15|17|18)\\d{9}$)|(^(400|800)\\d{7}(-\\d{1,6})?$)|(^(95013)\\d{6,8}$)"
        try {
            val p = Pattern.compile(phoneExp)
            val m = p.matcher(mobiles)
            IS_MOBEILPHONE = m.matches()
            if (IS_MOBEILPHONE) {
                return true
            }
        } catch (e: Exception) {
            e.stackTrace
        }
        return false
    }

//    fun isBlank(cs: CharSequence?): Boolean {
//        var strLen: Int
//        if (cs == null || cs.length.also { strLen = it } == 0) {
//            return true
//        }
//        for (i in 0 until strLen) {
//            if (!Character.isWhitespace(cs[i])) {
//                return false
//            }
//        }
//        return true
//    }

    fun isEmpty(cs: CharSequence?): Boolean {
        return cs == null || cs.length == 0
    }

    //生成随机数字和字母,
    fun getRandomStr(length: Int): CharSequence {
        val `val` = StringBuilder()
        val random = Random()

        //参数length，表示生成几位随机数
        for (i in 0 until length) {
            val charOrNum = if (random.nextInt(2) % 2 == 0) "char" else "num"
            //输出字母还是数字
            if ("char".equals(charOrNum, ignoreCase = true)) {
                //输出是大写字母还是小写字母，ASCII中 65～90号为26个大写英文字母，97～122号为26个小写英文字母，其余为一些标点符号、运算符号a
//                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;  //
                `val`.append((random.nextInt(26) + 97).toChar())
            } else if ("num".equals(charOrNum, ignoreCase = true)) {
                `val`.append(random.nextInt(10).toString())
            }
        }
        return `val`
    }

    fun replaceEach(
        text: String?,
        searchList: Array<String?>?,
        replacementList: Array<String?>?
    ): String? {
        return replaceEach(text, searchList, replacementList, false, 0)
    }

    /**
     * Returns a string whose value is this string, with any leading and trailing
     * LF removed.
     *
     * @param str
     * @return
     */
    fun trimLF(str: String): String {
        var str = str
        if (TextUtils.isEmpty(str)) return str
        str = str.trim { it <= ' ' }
        while (str.startsWith("\n")) {
            str = str.substring(1).trim { it <= ' ' }
        }
        while (str.endsWith("\n")) {
            str = str.substring(0, str.length - 1).trim { it <= ' ' }
        }
        return str
    }

    fun stripLF(str: String?): String? {
        if (!TextUtils.isEmpty(str)) {
//            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
//            Matcher m = p.matcher(str);
            // // 去除字符串中的空格、回车、换行符、制表符
//            Matcher m = Pattern.compile("\\s*|\t|\r|\n").matcher(str);
            val m = Pattern.compile("\t|\r|\n").matcher(str)
            return m.replaceAll("")
        }
        return str
    }

    private fun replaceEach(
        text: String?,
        searchList: Array<String?>?,
        replacementList: Array<String?>?,
        repeat: Boolean,
        timeToLive: Int
    ): String? {

        // mchyzer Performance note: This creates very few new objects (one major goal)
        // let me know if there are performance requests, we can create a harness to measure
        if (text == null || text.isEmpty() || searchList == null || searchList.size == 0 || replacementList == null || replacementList.size == 0) {
            return text
        }

        // if recursing, this shouldn't be less than 0
        check(timeToLive >= 0) {
            "Aborting to protect against StackOverflowError - " +
                    "output of one loop is the input of another"
        }
        val searchLength = searchList.size
        val replacementLength = replacementList.size

        // make sure lengths are ok, these need to be equal
        require(searchLength == replacementLength) {
            ("Search and Replace array lengths don't match: "
                    + searchLength
                    + " vs "
                    + replacementLength)
        }

        // keep track of which still have matches
        val noMoreMatchesForReplIndex = BooleanArray(searchLength)

        // index on index that the match was found
        var textIndex = -1
        var replaceIndex = -1
        var tempIndex = -1

        // index of replace array that will replace the search string found
        // NOTE: logic duplicated below START
        for (i in 0 until searchLength) {
            if (noMoreMatchesForReplIndex[i] || searchList[i] == null ||
                searchList[i]!!.isEmpty() || replacementList[i] == null
            ) {
                continue
            }
            tempIndex = text.indexOf(searchList[i]!!)

            // see if we need to keep searching for this
            if (tempIndex == -1) {
                noMoreMatchesForReplIndex[i] = true
            } else {
                if (textIndex == -1 || tempIndex < textIndex) {
                    textIndex = tempIndex
                    replaceIndex = i
                }
            }
        }
        // NOTE: logic mostly below END

        // no search strings found, we are done
        if (textIndex == -1) {
            return text
        }
        var start = 0

        // get a good guess on the size of the result buffer so it doesn't have to double if it goes over a bit
        var increase = 0

        // count the replacement text elements that are larger than their corresponding text being replaced
        for (i in searchList.indices) {
            if (searchList[i] == null || replacementList[i] == null) {
                continue
            }
            val greater = replacementList[i]!!.length - searchList[i]!!.length
            if (greater > 0) {
                increase += 3 * greater // assume 3 matches
            }
        }
        // have upper-bound at 20% increase, then let Java take over
        increase = Math.min(increase, text.length / 5)
        val buf = StringBuilder(text.length + increase)
        while (textIndex != -1) {
            for (i in start until textIndex) {
                buf.append(text[i])
            }
            buf.append(replacementList[replaceIndex])
            start = textIndex + searchList[replaceIndex]!!.length
            textIndex = -1
            replaceIndex = -1
            tempIndex = -1
            // find the next earliest match
            // NOTE: logic mostly duplicated above START
            for (i in 0 until searchLength) {
                if (noMoreMatchesForReplIndex[i] || searchList[i] == null ||
                    searchList[i]!!.isEmpty() || replacementList[i] == null
                ) {
                    continue
                }
                tempIndex = text.indexOf(searchList[i]!!, start)

                // see if we need to keep searching for this
                if (tempIndex == -1) {
                    noMoreMatchesForReplIndex[i] = true
                } else {
                    if (textIndex == -1 || tempIndex < textIndex) {
                        textIndex = tempIndex
                        replaceIndex = i
                    }
                }
            }
            // NOTE: logic duplicated above END
        }
        val textLength = text.length
        for (i in start until textLength) {
            buf.append(text[i])
        }
        val result = buf.toString()
        return if (!repeat) {
            result
        } else replaceEach(
            result,
            searchList,
            replacementList,
            repeat,
            timeToLive - 1
        )
    }


    fun removeHeadZero(text: String): String {
        return if (!TextUtils.isEmpty(text) && text.startsWith("0")) text.substring(1) else text
    }

    fun formatCurrency(text: String?): String {
        return formatCurrency(null, text)
    }

    fun formatCurrency(showCurrencyPrefix: Boolean, text: String?): String {
        return formatCurrency(if (showCurrencyPrefix) "Rp " else null, text)
    }

    fun formatCurrency(currencyPrefix: String?, text: String?): String {
        if (text == null) return "0"
        val num: Double
        num = try {
            text.toDouble()
        } catch (e: NumberFormatException) {
            return "0"
        }
        val format = NumberFormat.getNumberInstance(Locale("in", "ID"))
        format.maximumFractionDigits = 2
        if (text.contains(".")) { //如果格式化的数字字符含小数点 则保留2位小数
            format.minimumFractionDigits = 2
        }
        return if (TextUtils.isEmpty(currencyPrefix)) format.format(num) else currencyPrefix + format.format(
            num
        )
    }

    @Synchronized
    fun formatDate(context: Context?, date: Date?): String? {
        if (date == null) return null
        val delta = ((System.currentTimeMillis() - date.time) / 1000).toInt() //secs
        //        if (delta < 60)
//            return context.getResources().getQuantityString(R.plurals.x_minutes_ago, delta, delta);
        if (delta > 0 && delta < 24 * 60 * 60) { //小于24小时
            val calendar = Calendar.getInstance()
            val newDay = calendar[Calendar.DAY_OF_MONTH]
            calendar.time = date
            val day = calendar[Calendar.DAY_OF_MONTH]
            if (newDay == day) {
                return formatDate(context, "HH:mm", date)
            }
        }
        return formatDate(context, "HH:mm dd MMM yyyy", date)
    }

    fun formatDate(context: Context?, dateFormat: String?, date: Date?): String {
        return SimpleDateFormat(dateFormat, getSystemLocale(context!!)).format(date)
    }

    fun formatRemainWaitTime(context: Context?, secs: Long): String? {
        val df = DecimalFormat("00")
        var scale = 60
        if (secs < scale) {
            return String.format("00 : 00 : %s", df.format(secs))
        }
        if (secs < scale * 60) {
            return String.format("00 : %s : %s", df.format(secs / scale), df.format(secs % scale))
        }
        scale *= 60
        if (secs <= scale * 24) {
            val leftSec = secs % scale
            return String.format(
                "%s : %s : %s",
                df.format(secs / scale),
                df.format(leftSec / 60),
                df.format(leftSec % 60)
            )
        }
        val days = (secs / (scale * 24)).toInt()
        val leftSecs = secs % (scale * 24)

//        return String.format("%s %s", context.getResources().getQuantityString(R.plurals.x_days, days, days), formatRemainWaitTime(context, leftSecs));
        return null
    }

    fun trans2Spanned(source: String?): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Html.fromHtml(
            source,
            Html.FROM_HTML_MODE_LEGACY
        ) else Html.fromHtml(source)
    }

    fun generateCounterSpan(count: String, max: Int): SpannableString {
        val temp = SpannableString(String.format("%s/%s", count, max))
        temp.setSpan(
            ForegroundColorSpan(-0xcccccd),
            0,
            count.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return temp
    }

    fun isAuthenticatedImgUrl(value: String?): Boolean {
        return !TextUtils.isEmpty(value) && Pattern.compile("/image/authenticated/s--[\\w\\-]{8}--/")
            .matcher(value).find()
    }

    fun stripFetchUrlPrefix(value: String): String {
        return if (TextUtils.isEmpty(value) || !value.startsWith("https://res.cloudinary.com/dqgl4hkkx/image/fetch/c_fill,g_face,w_72,h_72/http")) value else value.replace(
            "https://res.cloudinary.com/dqgl4hkkx/image/fetch/c_fill,g_face,w_72,h_72/",
            ""
        )
    }

    fun formatUrlWthWatermark(sourceUrl: String): String {
        return if (!TextUtils.isEmpty(sourceUrl) && !sourceUrl.startsWith(watermark_fetch_domain)) {
            watermark_fetch_domain + sourceUrl
        } else sourceUrl
    }

    const val watermark_fetch_domain =
        "https://res.cloudinary.com/dqgl4hkkx/image/fetch/a_ignore,f_auto,q_auto/c_scale,f_auto,fl_relative,g_center,l_overlay_njgppi,w_0.7/"

//    fun isBase64Image(value: String): Boolean {
//        return !TextUtils.isEmpty(value) && value.matches("^(data:image/(png|jpeg|jpg|webp|gif|bmp);base64,).+")
//    }

    fun getNonNullVal(`val`: String?): String {
        return `val` ?: ""
    }

//    fun generateFileNameForBase64Image(base64String: String, imageStoragePath: String): String? {
//        if (!isBase64Image(base64String)) return null
//        val sp = base64String.split(",".toRegex()).toTypedArray()
//        var imageFormat = "jpg"
//        if (sp[0].contains("/") && sp[0].contains(";")) {
//            imageFormat = sp[0].substring(sp[0].indexOf("/") + 1, sp[0].indexOf(";"))
//        }
//        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
//        return imageStoragePath + "GudangView_Base64_" + timeStamp + "." + imageFormat
//    }


    /**
     * This is a text
     *
     * @param text
     * @return 将字符转为首字母大写 其余均为小写
     */
    fun getCapitalizeString(text: String): String {
        if (TextUtils.isEmpty(text)) return text
        val lowText = text.toLowerCase()
        return lowText.substring(0, 1).toUpperCase() + lowText.substring(1)
    }

    fun getDisplayNoticeCount(noticeCount: Int): String {
        return if (noticeCount > 99) "···" else noticeCount.toString()
    }

    fun getJsonObject(json: String?): JsonObject? {
        if (TextUtils.isEmpty(json)) return null
        val jsonObject = JsonParser().parse(json).asJsonObject
        return if (jsonObject.isJsonObject) jsonObject else null
    }

    fun maskMobile(mobile: String?): String? {
        if (mobile == null || mobile.length < 7) return null
        val len = mobile.length
        return if (len > 7) String.format(
            "%s **** %s",
            mobile.substring(0, 4),
            mobile.substring(len - 4, len)
        ) else String.format(
            "%s **** %s",
            mobile.substring(0, 3),
            mobile.substring(len - 4, len)
        )
    }

    fun trimStringByMaxLength(str: String?, maxLength: Int): String? {
        return if (str == null || str.length <= maxLength) {
            str
        } else String.format("%s...", str.substring(0, maxLength))
    }
}

private operator fun ByteArray.set(i: Int, value: Int) {

}
