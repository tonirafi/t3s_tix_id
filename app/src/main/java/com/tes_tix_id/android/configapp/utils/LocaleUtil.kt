package com.tes_tix_id.android.configapp.utils

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import android.preference.PreferenceManager
import java.util.*

object LocaleUtil {
    private const val SELECTED_LANGUAGE = "Locale.Selected.Language"
    fun onAttach(context: Context): Context {
        val lang = getPersistedData(context, getSystemLanguage(context))
        return setLocale(context, lang!!)
    }

    fun onAttach(context: Context, defaultLanguage: String): Context {
        val lang = getPersistedData(context, defaultLanguage)
        return setLocale(context, lang!!)
    }

    fun setLocale(context: Context, language: String): Context {
        persist(context, language)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, language)
        } else updateResourcesLegacy(
            context,
            language
        )
    }

    private fun getPersistedData(context: Context, defaultLanguage: String): String? {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(SELECTED_LANGUAGE, defaultLanguage)
    }

    private fun persist(context: Context, language: String) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString(SELECTED_LANGUAGE, language)
        editor.apply()
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context, language: String): Context {
        val locale = createLocale(language)
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        LocaleList.setDefault(configuration.locales)
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context.createConfigurationContext(configuration)
    }

    private fun updateResourcesLegacy(context: Context, language: String): Context {
        val locale = createLocale(language)
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = resources.configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale)
            configuration.setLayoutDirection(locale)
            context.createConfigurationContext(configuration)
        } else {
            configuration.locale = locale
        }
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }

    fun createLocale(language: String): Locale {
        return if ("in" == language) {
//            return new Locale(language, "ID");
            Locale(language)
        } else Locale("en", "US")

//        if ("en".equals(language)) {
//            return new Locale(language, "US");
//        }
    }

    /**
     * 获取app 当前语言环境
     *
     * @param context
     * @return
     */
    fun getLanguage(context: Context): String? {
        return getPersistedData(context, getSystemLanguage(context))
    }

    fun getSystemLanguage(context: Context): String {
        return getSystemLocale(context).language
    }

    fun getDisplayLanguage(context: Context): String {
//        if ("in".equals(getPersistedData(context, getSystemLanguage(context))))
//            return context.getString(R.string.indonesia);
//        return context.getString(R.string.english);
        return ""
    }

    /**
     * 从系统配置获取locale
     *
     * @return
     */
    fun getSystemLocale(context: Context): Locale {
        val resources =
            if (context.applicationContext == null) context.resources else context.applicationContext.resources
        return getLocaleFromConfiguration(resources.configuration)
    }

    /**
     * 从给定配置获取locale
     *
     * @param newConfig
     * @return
     */
    fun getLocaleFromConfiguration(newConfig: Configuration): Locale {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val ls = newConfig.locales
            return if (ls.isEmpty) Locale.ENGLISH else ls[0]
        }
        return newConfig.locale
    }

    /**
     * 获取系统默认语言环境
     *
     * @return
     */
    @get:Deprecated(
        """这是从 Java 来的方法，可以通过 Locale.setDefault(Locale) 来修改。
      不过这个方法有时候会出现莫名其妙的问题，比如一会儿返回系统语言 “zn_CN”，
      一会儿就返回 “en_US”，并不稳定。
      See {@link  LocaleUtil#getSystemLanguage(Context)}"""
    )
    val systemLanguage: String
        get() = systemLocale.language

    /**
     * 获取系统默认 locale
     *
     * @return
     */
    @get:Deprecated(
        """这是从 Java 来的方法，可以通过 Locale.setDefault(Locale) 来修改。
      不过这个方法有时候会出现莫名其妙的问题，比如一会儿返回系统语言 “zh_CN”，
      一会儿就返回 “en_US”，并不稳定。
      See {@link  LocaleUtil#getSystemLocale(Context)}"""
    )
    val systemLocale: Locale
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (LocaleList.getDefault().isEmpty) Locale.ENGLISH else LocaleList.getDefault()[0]
        } else Locale.getDefault()
}