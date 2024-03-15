package ru.eugeneprojects.passwordmanager.data

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceManager(context: Context) {

    private val preferences: SharedPreferences by lazy {
        context.getSharedPreferences(MASTER_PASSWORD_PREF, Context.MODE_PRIVATE)
    }

    fun isMasterPasswordExistInPref(): Boolean {
        return preferences.contains(MASTER_PASSWORD_VALUE)
    }

    fun getMasterPasswordExistInPref(): String? {
        return preferences.getString(MASTER_PASSWORD_VALUE, "")
    }

    fun saveMasterPasswordInPref(masterPassword: String) {
        preferences.edit().putString(MASTER_PASSWORD_VALUE, masterPassword).apply()
    }



    companion object {

        const val MASTER_PASSWORD_PREF = "MASTER_PASSWORD_PREF"
        const val MASTER_PASSWORD_VALUE = "MASTER_PASS_VALUE"
    }
}