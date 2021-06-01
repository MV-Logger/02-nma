package be.howest.maartenvercruysse.logger.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import be.howest.maartenvercruysse.logger.network.Book
import be.howest.maartenvercruysse.logger.network.Entry


@Entity
data class DatabaseBook(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String
)

@Entity
data class DatabaseEntry(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val text: String,
    @ColumnInfo(name = "when")
    val `when`: String,
    val where: String,
    val book_id: Int
)

fun List<Book>.asDatabaseModel(): List<DatabaseBook> {
    return map {
        DatabaseBook(
            id = it.id,
            name = it.name,
        )
    }
}


@JvmName("asDatabaseModelEntry")
fun List<Entry>.asDatabaseModel(): List<DatabaseEntry> {
    return map {
        DatabaseEntry(
            id = it.id,
            book_id = it.book_id,
            `when` = it.`when`,
            where = it.where,
            text = it.text
        )
    }
}
