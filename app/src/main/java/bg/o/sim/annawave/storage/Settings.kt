package bg.o.sim.annawave.storage

import android.content.Context
import bg.o.sim.annawave.ApplicationWrapper
import bg.o.sim.annawave.model.LoginPerson
import okhttp3.HttpUrl

private const val SHARED_PREFS_SETTINGS = "shPrefs_for_settings"
private const val SH_PREFS_KEY_BASE_URL = "baseUrl"
private const val SH_PREFS_LANG = "lang"
private const val DEFAULT_LANGUAGE = "en"
private const val NO_LANGUAGE_SET = "N\\A"
private fun getDefaultUrl() = HttpUrl.Builder().scheme("https").host("google.com").build()
private fun noUrlSet() = HttpUrl.Builder().host("N\\A").build()


/** Values that need to be stored in-memory during runtime, and should be globally accessible go 'ere.*/

var loggedInPerson: LoginPerson? = null

var lang: String = NO_LANGUAGE_SET
    get() {
        if (field == NO_LANGUAGE_SET) field = getSavedLanguage() ?: DEFAULT_LANGUAGE
        return field
    }
    set(value) {
        field = value
        saveLanguage(field)
    }

var baseUrl: HttpUrl = noUrlSet()
    get() {
        if (field == noUrlSet()) field = getSavedBaseUrl() ?: getDefaultUrl()
        return field
    }
    set(value) {
        field = value
        saveBaseUrl(field)
    }

fun setLanguage(language: String) {
    lang = language
    saveLanguage(lang)
}

fun saveBaseUrl(url: HttpUrl) {
    baseUrl = url
    ApplicationWrapper.accessibleInstance!!
        .getSharedPreferences(SHARED_PREFS_SETTINGS, Context.MODE_PRIVATE)
        .edit()
        .putString(SH_PREFS_KEY_BASE_URL, baseUrl.toString())
        .apply()
}

fun getSavedBaseUrl(): HttpUrl? = HttpUrl.parse(
    ApplicationWrapper.accessibleInstance!!
        .getSharedPreferences(SHARED_PREFS_SETTINGS, Context.MODE_PRIVATE)
        .getString(SH_PREFS_KEY_BASE_URL, "N\\A")!!
)

fun saveLanguage(language: String) {
    lang = language
    ApplicationWrapper.accessibleInstance!!
        .getSharedPreferences(SHARED_PREFS_SETTINGS, Context.MODE_PRIVATE)
        .edit()
        .putString(SH_PREFS_LANG, lang)
        .apply()
}

fun getSavedLanguage(): String? = ApplicationWrapper.accessibleInstance!!
    .getSharedPreferences(SHARED_PREFS_SETTINGS, Context.MODE_PRIVATE)
    .getString(SH_PREFS_KEY_BASE_URL, null)!!
