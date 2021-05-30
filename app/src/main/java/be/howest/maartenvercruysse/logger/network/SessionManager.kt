package be.howest.maartenvercruysse.logger.network

import android.content.Context
import android.content.SharedPreferences
import be.howest.maartenvercruysse.logger.R

/**
 * Session manager to save and fetch data from SharedPreferences
 */
class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    fun saveAuthToken(token: String, username: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.putString(USER, username)
        editor.apply()
    }

    fun removeAuthToken(){
        val editor = prefs.edit()
        editor.remove(USER_TOKEN)
        editor.remove(USER)
        editor.apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun fetchUsername(): String {
        return prefs.getString(USER, "")!!
    }

    companion object {
        const val USER_TOKEN = "user_token"
        const val USER = "username"
    }
}