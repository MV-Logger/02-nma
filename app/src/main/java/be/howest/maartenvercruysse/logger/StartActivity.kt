package be.howest.maartenvercruysse.logger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
    }

    fun moveToMain(){
        startActivity(Intent(this, MainActivity::class.java))
    }

    init{
        checkAuth
    }



}