package be.howest.maartenvercruysse.logger.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LoggerDao {

    @Query("select * from DatabaseBook")
    fun getBooks(): LiveData<List<DatabaseBook>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(videos: List<DatabaseBook>)
}

@Database(entities = [DatabaseBook::class], version = 1)
abstract class LoggerDatabase : RoomDatabase() {
    abstract val loggerDao: LoggerDao
}


private lateinit var INSTANCE: LoggerDatabase

fun getDatabase(context: Context): LoggerDatabase {
    synchronized(LoggerDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                LoggerDatabase::class.java,
                "Logger").build()
        }
    }
    return INSTANCE
}