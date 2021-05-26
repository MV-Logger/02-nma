package be.howest.maartenvercruysse.logger.repository

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import be.howest.maartenvercruysse.logger.MainActivity
import be.howest.maartenvercruysse.logger.network.ApiClient
import be.howest.maartenvercruysse.logger.network.SessionManager


private const val TOKEN = "TOKEN"

class LoggerRepository private constructor(context: Context) {
    private val appContext = context.applicationContext
    private var apiClient: ApiClient = ApiClient()
    private var sessionManager: SessionManager = SessionManager(context)

    fun checkAuth() {
        appContext.startActivity(Intent(appContext, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
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