package be.howest.maartenvercruysse.logger.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.howest.maartenvercruysse.logger.R
import be.howest.maartenvercruysse.logger.network.UserData
import be.howest.maartenvercruysse.logger.repository.LoggerRepository
import be.howest.maartenvercruysse.logger.ui.login.LoginFormState
import be.howest.maartenvercruysse.logger.ui.login.LoginResult
import kotlinx.coroutines.launch

class RegisterViewModel(val repo: LoggerRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _registerResult = MutableLiveData<RegisterResult>()
    val registerResult: LiveData<RegisterResult> = _registerResult

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun register(username: String, password: String) {
        viewModelScope.launch {
            repo.register(_registerResult, UserData(username, password))
        }
    }

    fun login(user: UserData) {
        viewModelScope.launch { repo.login(_loginResult, user) }
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