package be.howest.maartenvercruysse.logger.ui.books

import be.howest.maartenvercruysse.logger.database.DatabaseEntry

class BookCallback(private val block: (DatabaseEntry) -> Unit) {
    fun onClick(entry: DatabaseEntry) = block(entry)
}
