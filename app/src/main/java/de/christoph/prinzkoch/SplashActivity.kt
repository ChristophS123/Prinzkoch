package de.christoph.prinzkoch

import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_splash.*

private lateinit var auth: FirebaseAuth

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        init()
    }

    private fun init() {
        auth = Firebase.auth
        val currentUser = auth.currentUser
        hideStatusBar()
        val typeface:Typeface? = ResourcesCompat.getFont(this, R.font.carbonbl)
        tv_splash_title.typeface = typeface
        Handler(Looper.getMainLooper()).postDelayed({
            if(currentUser != null)
                startActivity(Intent(this@SplashActivity, LoggedInMainActivity::class.java))
            else
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }, 2500)
    }

    private fun hideStatusBar() {
        if(Build.VERSION.SDK_INT < 30) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        } else {
            window.setDecorFitsSystemWindows(false)
            val controller = window.insetsController
            if(controller != null) {
                controller!!.hide(WindowInsets.Type.statusBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_DEFAULT
            }
        }
    }

}