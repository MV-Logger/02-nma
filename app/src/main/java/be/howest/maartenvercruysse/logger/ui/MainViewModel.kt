package be.howest.maartenvercruysse.logger.ui

import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.howest.maartenvercruysse.logger.repository.LoggerRepository
import kotlinx.coroutines.launch


class MainViewModel(val repo: LoggerRepository) : ViewModel() {

    init {
        refreshRepo()
    }

    private fun refreshRepo() {
        viewModelScope.launch {
            repo.refreshBooks()
            repo.refreshEntries()
        }
    }



}