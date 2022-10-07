package de.christoph.prinzkoch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_log_in.*
import kotlinx.android.synthetic.main.activity_register.*

class LogInActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        setUpActionBar()
        cmd_log_in_submit.setOnClickListener { onLogInSubmitButtonClick() }
    }

    private fun onLogInSubmitButtonClick() {
        val email:String = et_login_email.text.toString()
        val password:String = et_login_password.text.toString()
        if(validateForm(email, password)) {
            showProgressDialog(resources.getString(R.string.please_wait))
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        hideProgressDialog()
                        Toast.makeText(this@LogInActivity, "Du hast dich erfolgreich eingeloggt", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@LogInActivity, LoggedInMainActivity::class.java))
                        finish()
                    } else {
                        hideProgressDialog()
                        showErrorSnackbar("Deine Email Adresse oder dein Passwort ist falsch.")
                    }
                }
        } else {
            showErrorSnackbar("Bitte gebe deine Email Adresse und dein Passwort ein.")
        }

    }

    private fun validateForm(email:String, password:String):Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_login_activity)
        toolbar_login_activity.setNavigationIcon(R.drawable.ic_back_pressed)
        toolbar_login_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

}