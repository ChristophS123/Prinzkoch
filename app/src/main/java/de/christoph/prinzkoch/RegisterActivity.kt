package de.christoph.prinzkoch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {

    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setUpActionBar()
        auth = Firebase.auth
        cmd_register_submit.setOnClickListener { registerNewUser() }
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_register_activity)
        toolbar_register_activity.setNavigationIcon(R.drawable.ic_back_pressed)
        toolbar_register_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun registerNewUser() {
        val name:String = et_register_name.text.toString().trim{it <= ' '}
        val email:String = et_register_email.text.toString().trim{it <= ' '}
        val password:String
        if(et_password.text.toString() == et_password_submit.text.toString()) {
            password = et_password.text.toString().trim{it <= ' '}
        } else {
            showErrorSnackbar("Die Passwort - Bestätigung war nicht korrekt")
            return
        }
        if(!validateForm(name, email, password))
            return
        showProgressDialog(resources.getString(R.string.please_wait))
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                hideProgressDialog()
                if(task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Willkommen $name, du bist nun Registriert",
                        Toast.LENGTH_LONG
                    ).show()
                    startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                    auth.signOut()
                    finish()
                } else {
                    showErrorSnackbar("Du konntest nicht registriert werden.")
                }
            }
    }

    private fun validateForm(name:String, email:String, password:String):Boolean {
        return when {
            TextUtils.isEmpty(name) -> {
                showErrorSnackbar("Bitte gebe einen Namen ein")
                return false
            }
            TextUtils.isEmpty(email) -> {
                showErrorSnackbar("Bitte gebe eine Email Adresse ein")
                return false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackbar("Bitte gebe ein Passwort ein")
                return false
            }
            else -> return true
        }
    }

}