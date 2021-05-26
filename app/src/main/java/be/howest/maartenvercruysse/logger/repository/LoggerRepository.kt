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

class LoggerRepository private constructor(context: Context) {
    private val appContext = context.applicationContext
    private var loggerService: LoggerService = ApiClient().getLoggerService(context)
    private var sessionManager: SessionManager = SessionManager(context)

    suspend fun checkAuth() {
        Log.d("test-auth", "b if")
        if (sessionManager.fetchAuthToken() != null) {
            withContext(Dispatchers.IO) {
                try {
                    val response = loggerService.authenticated()
                    Log.d("test-auth", "after resp")
                    if (response.isSuccessful) {
                        Log.d("test-auth", "succ resp")
                        appContext.startActivity(Intent(appContext, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                    }else{
                        Log.d("test-auth", "failed resp")
                        Log.d("test-auth", response.toString())
                        Log.d("test-auth", response.code().toString())
                        Log.d("test-auth", response.body().toString())
                    }
                    Unit
                } catch (e: Throwable) {
                    Log.d("test-auth", "failed")
                    Log.d("test-auth", e.stackTraceToString())
                }
            }
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

    suspend fun login(loginResult: MutableLiveData<LoginResult>, user: UserData) {
        withContext(Dispatchers.IO) {
            try {
                Log.d("test-token", "try")
                val response = loggerService.loginUser(user)

                if (response.isSuccessful) {
                    val token: Token = response.body()!!
                    sessionManager.saveAuthToken(token.access_token)
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