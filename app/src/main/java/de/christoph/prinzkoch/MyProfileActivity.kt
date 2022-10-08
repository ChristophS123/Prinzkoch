package de.christoph.prinzkoch

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import de.christoph.prinzkoch.firebase.FirestoreClass
import kotlinx.android.synthetic.main.activity_my_profile.*

class MyProfileActivity : BaseActivity() {

    private var imageUri:Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        setUpActionBar()
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getUserDataByID(this, FirebaseAuth.getInstance().currentUser!!.uid)
        iv_my_profile_activity.setOnClickListener { onProfileImageClick() }
        cmd_save_profile_changes.setOnClickListener { onSaveProfileChangesButtonClick() }
    }

    private fun onSaveProfileChangesButtonClick() {
        showProgressDialog(resources.getString(R.string.please_wait))
        if(imageUri != null) {
            val sRef:StorageReference = FirebaseStorage.getInstance().reference.child(
                "USER_IMAGE" + System.currentTimeMillis() + "." + getFileExtension(imageUri)
            )
            sRef.putFile(imageUri!!)
                .addOnSuccessListener { data ->
                    data.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener { uri ->
                            FirestoreClass().updateUserImage(this,
                                FirebaseAuth.getInstance().currentUser!!.uid,
                                uri.toString()
                            )
                        }
                        .addOnFailureListener {
                            showErrorSnackbar("Deine Änderung konnte nicht gespeichert werden. Überprüfe deine Internet Verbindung.")
                            hideProgressDialog()
                        }
                }
                .addOnFailureListener {
                    showErrorSnackbar("Deine Änderung konnte nicht gespeichert werden. Überprüfe deine Internet Verbindung.")
                    hideProgressDialog()
                }
        }
    }

    private fun getFileExtension(uri:Uri?):String? {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri!!))
    }

    fun updatedUserImage() {
        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun onProfileImageClick() {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            changeProfileImage()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_STORAGE_PERMISSION_CODE)
        }
    }

    private fun changeProfileImage() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == IMAGE_REQUEST_CODE && data!!.data != null) {
            imageUri = data!!.data
           try {
               Glide
                   .with(this)
                   .load(data!!.data)
                   .centerCrop()
                   .placeholder(R.drawable.ic_user_place_holder)
                   .into(iv_my_profile_activity)
           } catch (exception:Exception) {
               println(exception)
           }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == READ_STORAGE_PERMISSION_CODE) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                changeProfileImage()
            } else
                showErrorSnackbar("Die benötigten Berechtigungen wurden abgelehnt.")
        }
    }

    fun fillWithData(name:String, email:String, image:String) {
        et_my_profile_name.setText(name)
        et_my_profile_email.setText(email)
        Glide
            .with(this)
            .load(image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(iv_my_profile_activity)
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_my_profile_activity)
        val actionBar = supportActionBar
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_pressed)
            actionBar.title = resources.getString(R.string.log_in)
        }
        toolbar_my_profile_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    companion object {
        private const val READ_STORAGE_PERMISSION_CODE = 1
        private const val IMAGE_REQUEST_CODE = 2
    }

}