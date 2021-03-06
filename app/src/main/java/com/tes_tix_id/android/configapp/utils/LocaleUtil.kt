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
     * ??????app ??????????????????
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
     * ?????????????????????locale
     *
     * @return
     */
    fun getSystemLocale(context: Context): Locale {
        val resources =
            if (context.applicationContext == null) context.resources else context.applicationContext.resources
        return getLocaleFromConfiguration(resources.configuration)
    }

    /**
     * ?????????????????????locale
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
     * ??????????????????????????????
     *
     * @return
     */
    @get:Deprecated(
        """????????? Java ??????????????????????????? Locale.setDefault(Locale) ????????????
      ????????????????????????????????????????????????????????????????????????????????????????????? ???zn_CN??????
      ?????????????????? ???en_US?????????????????????
      See {@link  LocaleUtil#getSystemLanguage(Context)}"""
    )
    val systemLanguage: String
        get() = systemLocale.language

    /**
     * ?????????????????? locale
     *
     * @return
     */
    @get:Deprecated(
        """????????? Java ??????????????????????????? Locale.setDefault(Locale) ????????????
      ????????????????????????????????????????????????????????????????????????????????????????????? ???zh_CN??????
      ?????????????????? ???en_US?????????????????????
      See {@link  LocaleUtil#getSystemLocale(Context)}"""
    )
    val systemLocale: Locale
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (LocaleList.getDefault().isEmpty) Locale.ENGLISH else LocaleList.getDefault()[0]
        } else Locale.getDefault()
}