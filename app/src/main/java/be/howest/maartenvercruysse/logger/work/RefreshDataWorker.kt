package be.howest.maartenvercruysse.logger.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import be.howest.maartenvercruysse.logger.repository.LoggerRepository
import retrofit2.HttpException

class RefreshDataWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val repository = LoggerRepository.getInstance(applicationContext)

        try {
            repository.refreshEntries()
            repository.refreshBooks()
        } catch (e: HttpException) {
            return Result.retry()
        }
        return Result.success()
    }

    companion object {
        const val WORK_NAME = "be.howest.maartenvercruysse.logger.work.RefreshDataWorker"
    }
}