package be.howest.maartenvercruysse.logger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import be.howest.maartenvercruysse.logger.network.ApiClient
import be.howest.maartenvercruysse.logger.network.SessionManager

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
    }
}