package be.howest.maartenvercruysse.logger.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LoggerDao {

    @Query("select * from DatabaseBook")
    fun getBooks(): LiveData<List<DatabaseBook>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllBooks(books: List<DatabaseBook>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllEntries(entries: List<DatabaseEntry>)

    @Query("Select *  from DatabaseEntry where book_id = :id")
    fun getEntriesFromBook(id: Int): LiveData<List<DatabaseEntry>>
}


@Database(entities = [DatabaseBook::class, DatabaseEntry::class  ], version = 3, exportSchema = false)
abstract class LoggerDatabase : RoomDatabase() {
    abstract val loggerDao: LoggerDao
}


private lateinit var INSTANCE: LoggerDatabase

fun getDatabase(context: Context): LoggerDatabase {
    synchronized(LoggerDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                LoggerDatabase::class.java,
                "Logger")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}