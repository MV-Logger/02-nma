package be.howest.maartenvercruysse.logger.network

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


class ApiClient {
    private lateinit var loggerService: LoggerService

    fun getLoggerService(context: Context): LoggerService {

        // Initialize LoggerService if not initialized yet
        if (!::loggerService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.NODE_API_SERVER)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(okhttpClient(context))
                .build()

            loggerService = retrofit.create(LoggerService::class.java)
        }

        return loggerService
    }

    /**
     * Initialize OkhttpClient with our interceptor
     */
    private fun okhttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)  // increase timeout from 10s (default) to 30s
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(AuthInterceptor(context))
            .build()
    }

}


object Constants {
    const val NODE_API_SERVER = "https://nodejs-03.herokuapp.com/api/"
    const val LARAVEL_API_SERVER = "https://nodejs-03.herokuapp.com/api/"
}