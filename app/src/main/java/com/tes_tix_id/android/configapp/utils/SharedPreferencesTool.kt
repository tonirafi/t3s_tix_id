package com.tes_tix_id.android.configapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class SharedPreferencesTool private constructor(context: Context) {
    private val sp: SharedPreferences
    private var mEditor: SharedPreferences.Editor? = null
    private var mGson: Gson? = null
    private val gson: Gson?
        private get() {
            if (mGson == null) mGson =
                GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create()
            return mGson
        }
    val editor: SharedPreferences.Editor?
        get() {
            if (mEditor == null) mEditor = sp.edit()
            return mEditor
        }

    fun getBoolean(key: String?, value: Boolean): Boolean {
        return sp.getBoolean(key, value)
    }

    fun getInt(key: String?, value: Int): Int {
        return sp.getInt(key, value)
    }

    fun getLong(key: String?, value: Long): Long {
        return sp.getLong(key, value)
    }

    fun getString(key: String?, value: String?): String? {
        return sp.getString(key, value)
    }

    fun getFloat(key: String?, value: Float): Float {
        return sp.getFloat(key, value)
    }

    fun putBoolean(key: String?, value: Boolean) {
        val editor = editor
        editor!!.putBoolean(key, value)
        editor.apply()
    }

    fun putInt(key: String?, value: Int) {
        val editor = editor
        editor!!.putInt(key, value)
        editor.apply()
    }

    fun putLong(key: String?, value: Long) {
        val editor = editor
        editor!!.putLong(key, value)
        editor.apply()
    }

    fun putString(key: String?, value: String?) {
        val editor = editor
        editor!!.putString(key, value)
        editor.apply()
    }

    fun putFloat(key: String?, value: Float) {
        val editor = editor
        editor!!.putFloat(key, value)
        editor.apply()
    }

    fun putObject(key: String?, `object`: Any) {
        val json = gson!!.toJson(`object`)
        putString(key, json)
    }

    fun putObject(`object`: Any) {
        val key = getKeyFromClass(`object`.javaClass)
        putObject(key, `object`)
    }

    fun <T> getObject(key: String?, clazz: Class<T>?, defaultObject: T): T {
        val json = getString(key, null)
        if (json == null || json.isEmpty()) return defaultObject
        val `object`: T = gson!!.fromJson(json, TypeToken.get(clazz).type)
        //        T object = getGson().fromJson(json, clazz);
        return `object` ?: defaultObject
    }

    fun <T> getObject(clazz: Class<T>, defaultObject: T): T {
        return getObject(getKeyFromClass(clazz), clazz, defaultObject)
    }

    private fun getKeyFromClass(aClass: Class<*>): String {
        return aClass.name.toUpperCase()
    }

    fun remove(key: String?) {
        val editor = editor
        editor!!.remove(key)
        editor.apply()
    }

//    fun remove(`object`: Any) {
//        val key = getKeyFromClass(`object`.javaClass)
//        remove(key)
//    }
//
//    fun remove(aClass: Class<*>) {
//        remove(aClass.name.toUpperCase())
//    }

    companion object {
        private var instance: SharedPreferencesTool? = null
        fun getInstance(context: Context): SharedPreferencesTool? {
            if (instance == null) instance = SharedPreferencesTool(context.applicationContext)
            return instance
        }
    }

    init {
        sp = PreferenceManager.getDefaultSharedPreferences(context)
    }
}