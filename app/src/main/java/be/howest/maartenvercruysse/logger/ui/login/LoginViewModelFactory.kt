package be.howest.maartenvercruysse.logger.ui.login

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.howest.maartenvercruysse.logger.repository.LoggerRepository

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class LoginViewModelFactory(val app: Application) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(LoggerRepository.getInstance(app.applicationContext)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}