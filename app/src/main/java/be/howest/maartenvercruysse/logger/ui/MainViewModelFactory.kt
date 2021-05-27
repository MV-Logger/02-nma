package be.howest.maartenvercruysse.logger.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.howest.maartenvercruysse.logger.repository.LoggerRepository
import be.howest.maartenvercruysse.logger.ui.register.RegisterViewModel

class MainViewModelFactory(val app: Application): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return MainViewModel(LoggerRepository.getInstance(app.applicationContext)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}