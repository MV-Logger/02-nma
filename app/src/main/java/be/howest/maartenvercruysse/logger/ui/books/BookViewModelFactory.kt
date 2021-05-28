package be.howest.maartenvercruysse.logger.ui.books

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.howest.maartenvercruysse.logger.repository.LoggerRepository

class BookViewModelFactory(val app: Application, val id: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookViewModel(LoggerRepository.getInstance(app.applicationContext), id) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }
}