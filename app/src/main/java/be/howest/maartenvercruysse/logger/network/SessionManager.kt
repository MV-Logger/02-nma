package be.howest.maartenvercruysse.logger.network

import android.content.Context
import android.content.SharedPreferences
import be.howest.maartenvercruysse.logger.R

/**
 * Session manager to save and fetch data from SharedPreferences
 */
class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    /**
     * Function to save auth token
     */
    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    /**
     * Function to fetch auth token
     */
    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    companion object {
        const val USER_TOKEN = "user_token"
    }
}