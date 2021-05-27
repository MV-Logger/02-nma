package be.howest.maartenvercruysse.logger.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import be.howest.maartenvercruysse.logger.network.Book


@Entity
data class DatabaseBook(
    @PrimaryKey
    val id: Int,
    val name: String

)


fun List<Book>.asDatabaseModel(): List<DatabaseBook> {
    return map {
        DatabaseBook(
            id = it.id,
            name = it.name,
           )
    }
}