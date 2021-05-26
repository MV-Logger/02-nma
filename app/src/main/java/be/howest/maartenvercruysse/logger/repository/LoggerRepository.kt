package be.howest.maartenvercruysse.logger.repository

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import be.howest.maartenvercruysse.logger.MainActivity
import be.howest.maartenvercruysse.logger.R
import be.howest.maartenvercruysse.logger.network.*
import be.howest.maartenvercruysse.logger.ui.login.LoggedInUserView
import be.howest.maartenvercruysse.logger.ui.login.LoginResult
import be.howest.maartenvercruysse.logger.ui.register.RegisterResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


private const val TOKEN = "TOKEN"

class LoggerRepository private constructor(context: Context) {
    private val appContext = context.applicationContext
    private var loggerService: LoggerService = ApiClient().getLoggerService(context)
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

    suspend fun login(loginResult: MutableLiveData<LoginResult>, user: UserData) {
        withContext(Dispatchers.IO) {
            try {
                Log.d("test-token", "try")
                val response = loggerService.loginUser(user)

                if (response.isSuccessful) {
                    val token: Token? = response.body()
                    sessionManager.saveAuthToken(token.toString())
                    loginResult.postValue(LoginResult(success = LoggedInUserView(displayName = user.username)))
                } else {
                    loginResult.postValue(LoginResult(error = R.string.login_failed))
                }
            } catch (e: Throwable) {
                Log.d("test-token", e.stackTraceToString())
                loginResult.postValue(LoginResult(error = R.string.login_failed))
            }
        }
    }

    suspend fun register(registerResult: MutableLiveData<RegisterResult>, user: UserData) {
        withContext(Dispatchers.IO) {
            try {
                val response = loggerService.registerUser(user)

                if (response.isSuccessful) {
                    registerResult.postValue(RegisterResult(success = user))
                } else {
                    registerResult.postValue(RegisterResult(error = R.string.invalid_username))
                }
            } catch (e: Throwable) {
                Log.d("test-token", e.stackTraceToString())
                registerResult.postValue(RegisterResult(error = R.string.invalid_username))
            }

        }
    }
}