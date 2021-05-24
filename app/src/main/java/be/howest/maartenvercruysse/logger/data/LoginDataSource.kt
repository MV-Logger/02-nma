package be.howest.maartenvercruysse.logger.data

import android.util.Log
import be.howest.maartenvercruysse.logger.data.model.LoggedInUser
import be.howest.maartenvercruysse.logger.network.LoggerNetwork
import be.howest.maartenvercruysse.logger.network.Token
import be.howest.maartenvercruysse.logger.network.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.lang.Exception

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

//    fun login(username: String, password: String): Result<LoggedInUser> {
//
//
//
//    }

    fun logout() {
        // TODO: revoke authentication
    }
}