package ru.eugeneprojects.passwordmanager.data

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPreferenceManager @Inject constructor(@ApplicationContext context: Context) {

    private val masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val preferences: SharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            context,
            MASTER_PASSWORD_PREF,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
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