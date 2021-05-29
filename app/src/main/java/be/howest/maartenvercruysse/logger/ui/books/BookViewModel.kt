package be.howest.maartenvercruysse.logger.ui.books

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import be.howest.maartenvercruysse.logger.database.DatabaseEntry
import be.howest.maartenvercruysse.logger.repository.LoggerRepository

class BookViewModel(repo: LoggerRepository, id: Int): ViewModel() {

    val entries: LiveData<List<DatabaseEntry>> = repo.getEntriesFromBook(id)

    init{
        Log.d("book", "view model created with id: $id")
    }
}