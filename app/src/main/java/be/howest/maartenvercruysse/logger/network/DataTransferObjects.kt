package be.howest.maartenvercruysse.logger.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserData(var username: String, var password: String)

@JsonClass(generateAdapter = true)
data class Token(var access_token: String)

@JsonClass(generateAdapter = true)
data class Book(var id: Int, var name: String)

@JsonClass(generateAdapter = true)
data class Entry(var id: Int, var book_id: Int, var text: String, var where: String, @Json(name = "when") var `when`: String)
