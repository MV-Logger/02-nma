package be.howest.maartenvercruysse.logger.network

import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit


interface LoggerService {
    @POST("auth/register")
    suspend fun registerUser(@Body user: UserData): Response<Void>

    @POST("auth/login")
    suspend fun loginUser(@Body user: UserData): Response<Token>
}


object LoggerNetwork {


    private val client: OkHttpClient = OkHttpClient.Builder() // increase timeout from 10s (default) to 30s
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://nodejs-03.herokuapp.com/api/")
        .addConverterFactory(MoshiConverterFactory.create())
        .client(client)
        .build()

    val Logger = retrofit.create(LoggerService::class.java)
}
