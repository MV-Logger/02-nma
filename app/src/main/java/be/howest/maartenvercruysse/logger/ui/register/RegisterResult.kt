package be.howest.maartenvercruysse.logger.ui.register

import be.howest.maartenvercruysse.logger.network.UserData
/**
 * Authentication result : success (user details) or error message.
 */
data class RegisterResult(
    val success: UserData? = null,
    val error: Int? = null
)