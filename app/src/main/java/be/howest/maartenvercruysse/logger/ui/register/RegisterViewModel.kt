package be.howest.maartenvercruysse.logger.ui.register

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*

import be.howest.maartenvercruysse.logger.R
import be.howest.maartenvercruysse.logger.network.LoggerNetwork
import be.howest.maartenvercruysse.logger.network.Token
import be.howest.maartenvercruysse.logger.network.UserData
import be.howest.maartenvercruysse.logger.repository.LoggerRepository
import be.howest.maartenvercruysse.logger.ui.login.LoggedInUserView
import be.howest.maartenvercruysse.logger.ui.login.LoginFormState
import be.howest.maartenvercruysse.logger.ui.login.LoginResult
import kotlinx.coroutines.launch

class RegisterViewModel(private val repo: LoggerRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _registerResult = MutableLiveData<RegisterResult>()
    val registerResult: LiveData<RegisterResult> = _registerResult

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun register(username: String, password: String) {

        viewModelScope.launch {

            try {
                val user = UserData(username, password)
                val response = LoggerNetwork.Logger.registerUser(user)

                if (response.isSuccessful) {
                    _registerResult.value = RegisterResult(success = user)
                } else {
                    _registerResult.value = RegisterResult(error = R.string.invalid_username)
                }
            } catch (e: Throwable) {
                Log.d("test-token", e.stackTraceToString())
                _registerResult.value = RegisterResult(error = R.string.invalid_username)
            }

        }
    }

    fun login(user: UserData) {
        viewModelScope.launch {

            try {
                val response = LoggerNetwork.Logger.loginUser(user)

                if (response.isSuccessful) {
                    val token: Token? = response.body()
                    repo.storeToken(token.toString())
                    _loginResult.value = LoginResult(success = LoggedInUserView(user.username))
                } else {
                    _loginResult.value = LoginResult(error = R.string.login_failed)
                }
            } catch (e: Throwable) {
                Log.d("test-token", e.stackTraceToString())
                _loginResult.value = LoginResult(error = R.string.login_failed)
            }

        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password_login)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return username.isNotBlank()
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 4
    }


}