package be.howest.maartenvercruysse.logger.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface LoggerService {
    @POST("auth/register")
    suspend fun registerUser(@Body user: UserData): Response<Void>

    @POST("auth/login")
    suspend fun loginUser(@Body user: UserData): Response<Token>

    @GET("auth/authenticated")
    suspend fun authenticated(): Response<Void>

    @GET("books")
    suspend fun getBooks(): List<Book>

    @POST("books")
    suspend fun addBook(@Body book: Book): Response<Void>

    @GET("books/{id}/entries")
    suspend fun getEntries(@Path("id") id: Int): List<Entry>
}
