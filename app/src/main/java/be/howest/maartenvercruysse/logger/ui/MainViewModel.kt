package be.howest.maartenvercruysse.logger.ui

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
            repo.refreshAll()
        }
    }



}