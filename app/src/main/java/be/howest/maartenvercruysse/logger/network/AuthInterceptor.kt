package be.howest.maartenvercruysse.logger.network

import android.content.Context
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor to add auth token to requests
 */
class AuthInterceptor(context: Context) : Interceptor {
    private val sessionManager = SessionManager(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        // If token has been saved, add it to the request
        sessionManager.fetchAuthToken()?.let {
            Log.d("test-auth", it)
            Log.d("test-auth", "Bearer $it")
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}