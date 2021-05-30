package be.howest.maartenvercruysse.logger.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.howest.maartenvercruysse.logger.R
import be.howest.maartenvercruysse.logger.network.UserData
import be.howest.maartenvercruysse.logger.repository.LoggerRepository
import kotlinx.coroutines.launch

class LoginViewModel(val repo: LoggerRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    val startupFlag = MutableLiveData(true) // need to fire function only once on startup but need access to repo

    fun login(username: String, password: String) {
        viewModelScope.launch { repo.login(_loginResult, UserData(username, password)) }
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

    suspend fun checkAuth() {
        repo.checkAuth(_loginResult)
    }
}