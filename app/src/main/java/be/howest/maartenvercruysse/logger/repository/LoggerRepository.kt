package be.howest.maartenvercruysse.logger.repository

import android.content.Context
import android.content.SharedPreferences


private const val TOKEN = "TOKEN"

class LoggerRepository private constructor(context: Context) {
    private val sharedPreferences = context.applicationContext.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)


    suspend fun checkAuth() {

    }

    fun storeToken(token: String) {
        with(sharedPreferences.edit()) {
            putString(TOKEN, token)
            apply()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: LoggerRepository? = null

        fun getInstance(context: Context): LoggerRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE?.let {
                    return it
                }

                val instance = LoggerRepository(context)
                INSTANCE = instance
                instance
            }
        }
    }
}