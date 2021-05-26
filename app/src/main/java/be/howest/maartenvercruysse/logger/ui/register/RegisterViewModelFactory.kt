package be.howest.maartenvercruysse.logger.ui.register

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.howest.maartenvercruysse.logger.repository.LoggerRepository

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class RegisterViewModelFactory(val app: Application) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(LoggerRepository.getInstance(app.applicationContext)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}