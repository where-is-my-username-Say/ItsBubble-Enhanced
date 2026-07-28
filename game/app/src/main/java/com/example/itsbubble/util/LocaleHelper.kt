package com.example.itsbubble.util

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import java.util.*

object LocaleHelper {
    private const val PREFS_NAME = "ItsBubblePrefs"
    private const val KEY_LANGUAGE = "app_language"

    private val SUPPORTED_LANGUAGES = listOf("en", "zh", "es", "ja", "ko", "ru", "de")

    fun setLocale(context: Context, languageCode: String): Context {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LANGUAGE, languageCode).apply()
        return updateResources(context, languageCode)
    }

    fun getSavedLanguage(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_LANGUAGE, "en") ?: "en"
    }

    fun getSupportedLanguages(): List<Pair<String, String>> = listOf(
        "en" to "English",
        "zh" to "中文",
        "es" to "Español",
        "ja" to "日本語",
        "ko" to "한국어",
        "ru" to "Русский",
        "de" to "Deutsch"
    )

    fun onAttach(context: Context): Context {
        val lang = getSavedLanguage(context)
        return updateResources(context, lang)
    }

    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        return context.createConfigurationContext(config)
    }
}
