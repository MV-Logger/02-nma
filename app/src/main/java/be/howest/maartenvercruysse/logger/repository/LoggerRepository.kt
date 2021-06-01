package be.howest.maartenvercruysse.logger.repository

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import be.howest.maartenvercruysse.logger.R
import be.howest.maartenvercruysse.logger.StartActivity
import be.howest.maartenvercruysse.logger.database.DatabaseBook
import be.howest.maartenvercruysse.logger.database.DatabaseEntry
import be.howest.maartenvercruysse.logger.database.asDatabaseModel
import be.howest.maartenvercruysse.logger.database.getDatabase
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
    private val database = getDatabase(context)

    var books: LiveData<List<DatabaseBook>> = database.loggerDao.getBooks()

    suspend fun checkAuth(loginResult: MutableLiveData<LoginResult>, loadingProgressBar: ProgressBar) {
        if (sessionManager.fetchAuthToken() != null) {
            withContext(Dispatchers.IO) {
                try {
                    loadingProgressBar.visibility = View.GONE
                    val response = loggerService.authenticated()
                    if (response.isSuccessful) {
                        loginResult.postValue(LoginResult(success = LoggedInUserView(displayName = sessionManager.fetchUsername())))
                    } else {
                        loginResult.postValue(LoginResult(error = R.string.login_failed))
                    }
                    Unit
                } catch (e: Throwable) {
                    Log.d("test-auth", e.stackTraceToString())
                    loginResult.postValue(LoginResult(error = R.string.no_internet))
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
                val response = loggerService.loginUser(user)

                if (response.isSuccessful) {
                    val token: Token = response.body()!!
                    sessionManager.saveAuthToken(token.access_token, user.username)
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

    fun logout() {
        sessionManager.removeAuthToken()
        appContext.startActivity(Intent(appContext, StartActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    suspend fun refreshAll() {
        withContext(Dispatchers.IO) {
            database.clearAllTables()
            refreshBooks()
            refreshEntries()
        }
    }

    private suspend fun refreshBooks() {
        withContext(Dispatchers.IO) {
            database.loggerDao.insertAllBooks(loggerService.getBooks().asDatabaseModel())
        }
    }

    private suspend fun refreshEntries() {
        withContext(Dispatchers.IO) {
            Log.d("book", "books: " + books.value?.size)
            loggerService.getBooks().forEach {
                refreshEntry(it.id)
            }
        }
    }

    private suspend fun refreshEntry(id: Int) {
        withContext(Dispatchers.IO) {
            database.loggerDao.insertAllEntries(loggerService.getEntries(id).asDatabaseModel())
        }
    }

    suspend fun addBook(book: Book) {
        withContext(Dispatchers.IO) {
            try {
                loggerService.addBook(book)
                refreshBooks()
            } catch (e: Throwable) {
                Log.d("book", e.stackTraceToString())
            }
        }
    }

    fun getEntriesFromBook(id: Int): LiveData<List<DatabaseEntry>> {
        return database.loggerDao.getEntriesFromBook(id)
    }

    suspend fun addEntry(id: Int, entry: Entry) {
        withContext(Dispatchers.IO) {
            try {
                loggerService.addEntry(id, entry)
                refreshEntry(id)
            } catch (e: Throwable) {
                Log.d("book", e.stackTraceToString())
            }
        }
    }

    fun getUsername(): String {
        return sessionManager.fetchUsername()
    }
}


