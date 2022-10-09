package de.christoph.prinzkoch

import android.app.Activity
import android.content.ClipDescription
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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.christoph.prinzkoch.constants.Constants
import de.christoph.prinzkoch.firebase.FirestoreClass
import de.christoph.prinzkoch.models.Recipe
import kotlinx.android.synthetic.main.activity_create_new_recipe.*

class CreateNewRecipe : BaseActivity() {

    private var imageUri:Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_recipe)
        setUpActionBar()
        iv_create_new_recipe.setOnClickListener { onCreateNewRecipeImageViewClicked() }
        cmd_new_recipe_submit.setOnClickListener { onNewRecipeSubmitClick() }
    }

    private fun onNewRecipeSubmitClick() {
        showProgressDialog(resources.getString(R.string.please_wait))
        val nameOfRecipe:String = et_new_recipe_name_of_recipe.text.toString()
        val shortDescription:String = et_new_recipe_short_description.text.toString()
        val category:String? = getRecipeCategory()
        val longDescription:String = et_new_recipe_long_description.text.toString()
        if(validateForm(nameOfRecipe, shortDescription, category, longDescription)) {
            uploadRecipeImageToStorage(nameOfRecipe, shortDescription, category!!, longDescription)
        } else {
            hideProgressDialog()
            showErrorSnackbar("Bitte fülle alle Felder aus")
        }
    }

    private fun createNewRecipe(nameOfRecipe: String, shortDescription: String, category: String, longDescription: String, image: Uri?) {
        FirestoreClass().getUserNameAndImageToCreateRecipe(this, nameOfRecipe, shortDescription, category, longDescription, image)
    }

    fun createNewRecipeWithNameAndImageFromUser(nameOfRecipe: String, shortDescription: String, category: String, longDescription: String, image: Uri?, userName:String, userImage:String) {
        val recipe:Recipe = Recipe(nameOfRecipe, shortDescription, category, longDescription, image.toString(), FirebaseAuth.getInstance().currentUser!!.uid, userName, userImage)
        FirestoreClass().saveNewRecipe(this, recipe)
    }

    private fun uploadRecipeImageToStorage(nameOfRecipe: String, shortDescription: String, category: String, longDescription: String) {
        var returnUri:Uri? = null
            val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
                "RECIPE_IMAGE" + System.currentTimeMillis() + "." + getFileExtension(imageUri)
            )
            sRef.putFile(imageUri!!)
                .addOnSuccessListener { data ->
                    data.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener { uri ->
                            createNewRecipe(nameOfRecipe, shortDescription, category, longDescription, uri)
                        }
                        .addOnFailureListener {
                            showErrorSnackbar("Das Bild konnte nicht hochgeladen werden. Überprüfe deine Internet Verbindung.")
                        }
                }
                .addOnFailureListener {
                    showErrorSnackbar("Das Bild konnte nicht hochgeladen werden. Überprüfe deine Internet Verbindung.")
                }
    }

    private fun getFileExtension(uri:Uri?):String? {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri!!))
    }

    private fun getRecipeCategory(): String? {
        return if(rb_breakfast.isChecked)
            Constants.BREAKFAST
        else if(rb_lunch.isChecked)
            Constants.LUNCH
        else if(rb_diner.isChecked)
            Constants.DINER
        else if(rb_snacks.isChecked)
            Constants.SNACKS
        else
            null
    }

    private fun validateForm(nameOfRecipe:String, shortDescription:String, category:String?, longDescription:String): Boolean {
        return nameOfRecipe.isNotEmpty() && shortDescription.isNotEmpty() && category != null && longDescription.isNotEmpty()
    }

    private fun onCreateNewRecipeImageViewClicked() {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            changeRecipeImage()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_STORAGE_PERMISSION_CODE
            )
        }
    }

    private fun changeRecipeImage() {
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
                    .into(iv_create_new_recipe)
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
                changeRecipeImage()
            } else
                showErrorSnackbar("Die benötigten Berechtigungen wurden abgelehnt.")
        }
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_new_recipe_activity)
        val actionBar = supportActionBar
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_pressed)
            actionBar.title = resources.getString(R.string.create_new_recipe)
        }
        toolbar_new_recipe_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    fun recipeCreatedSuccesfully() {
        hideProgressDialog()
        Toast.makeText(this, "Du hast ein neues Rezept erstellt", Toast.LENGTH_LONG).show()
        setResult(Activity.RESULT_OK)
        finish()
    }

    companion object {
        const val READ_STORAGE_PERMISSION_CODE = 100
        const val IMAGE_REQUEST_CODE = 101
    }

}