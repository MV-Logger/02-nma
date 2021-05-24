package be.howest.maartenvercruysse.logger.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import be.howest.maartenvercruysse.logger.data.LoginRepository
import be.howest.maartenvercruysse.logger.data.Result

import be.howest.maartenvercruysse.logger.R
import be.howest.maartenvercruysse.logger.data.model.LoggedInUser
import be.howest.maartenvercruysse.logger.network.LoggerNetwork
import be.howest.maartenvercruysse.logger.network.Token
import be.howest.maartenvercruysse.logger.network.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.lang.Exception

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
//        val result = loginRepository.login(username, password)
//
//        if (result is Result.Success) {
//            _loginResult.value = LoginResult(success = LoggedInUserView(displayName = result.data.username))
//        } else {
//            _loginResult.value = LoginResult(error = R.string.login_failed)
//        }

        viewModelScope.launch {

            try {
                Log.d("test-token", "try")
                val response = LoggerNetwork.Logger.loginUser(UserData(username, password))

                if (response.isSuccessful) {
                    val token: Token? = response.body()
                    Log.d("test-token", token.toString())
                    _loginResult.value = LoginResult(success = LoggedInUserView(displayName = username))
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
        return password.isNotEmpty()
    }
}