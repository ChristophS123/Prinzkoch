package de.christoph.prinzkoch.firebase

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import de.christoph.prinzkoch.*
import de.christoph.prinzkoch.constants.Constants
import de.christoph.prinzkoch.models.Recipe
import de.christoph.prinzkoch.models.User

class FirestoreClass {

    private val mFirestore = Firebase.firestore;

    fun saveNewUser(activity:RegisterActivity, id:String, name:String, email:String) {
        mFirestore.collection(Constants.USERS)
            .document(id)
            .set(User(
                id,
                name,
                email,
                ""
            ))
            .addOnSuccessListener {
                activity.succesfullySignedUp(name)
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
                activity.showErrorSnackbar("Du konntest nicht registriert werden.")
            }
    }

    fun getUserDataByID(activity:MyProfileActivity, id:String) {
        mFirestore.collection(Constants.USERS)
            .document(id)
            .get()
            .addOnSuccessListener { data ->
                val user: User? = data.toObject(User::class.java)
                if(user != null) {
                    activity.hideProgressDialog()
                    activity.fillWithData(user!!.name, user!!.email, user!!.image)
                } else {
                    activity.hideProgressDialog()
                    activity.showErrorSnackbar("Deine Daten konnten nicht geladen werden. Überprüfe deine Internet Verbindung.")
                }
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
                activity.showErrorSnackbar("Deine Daten konnten nicht geladen werden. Überprüfe deine Internet Verbindung.")
            }
    }

    fun updateUserImage(activity:MyProfileActivity, id: String, image:String) {
        mFirestore.collection(Constants.USERS)
            .document(id)
            .update(Constants.IMAGE, image)
            .addOnSuccessListener {
                activity.updatedUserImage()
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
                activity.showErrorSnackbar("Deine Daten konnten nicht gespeichert werden. Überprüfe deine Internet Verbindung.")
            }
    }

    fun saveNewRecipe(activity: CreateNewRecipe, recipe:Recipe) {
        mFirestore.collection(Constants.RECIPES)
            .document()
            .set(recipe)
            .addOnSuccessListener {
                activity.recipeCreatedSuccesfully()
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
                activity.showErrorSnackbar("Dein Rezept konnte nicht erstellt werden. Überprüfe deine Internet Verbindung.")
            }
    }

    fun getUserNameAndImageByID(activity: LoggedInMainActivity, id:String) {
        mFirestore.collection(Constants.USERS)
            .document(id)
            .get()
            .addOnSuccessListener { data ->
                val user:User? = data.toObject(User::class.java)
                if(user != null) {
                    activity.fillInformationsToNavMenu(user!!.name, user!!.image)
                } else
                    activity.showProgressDialog("Deine Daten konnten nicht geladen werden.")
            }
            .addOnFailureListener {
                activity.showErrorSnackbar("Deine Daten konnten nicht geladen werden. Überprüfe deine Internet Verbindung.")
            }
    }

    fun getUserNameAndImageToCreateRecipe(
        activity: CreateNewRecipe,
        nameOfRecipe: String,
        shortDescription: String,
        category: String,
        longDescription: String,
        image: Uri?
    ) {
        mFirestore.collection(Constants.USERS)
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .get()
            .addOnSuccessListener { recipe ->
                val model:User = recipe.toObject(User::class.java)!!
                activity.createNewRecipeWithNameAndImageFromUser(nameOfRecipe, shortDescription, category, longDescription, image, model.name, model.image)
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
                activity.showErrorSnackbar("Das Rezept konnte nicht erstellt werden. Überprüfe deine Internet Verbindung.")
            }
    }

    fun loadAllRecipes(activity: LoggedInMainActivity) {
        mFirestore.collection(Constants.RECIPES)
            .get()
            .addOnSuccessListener { document ->
                var loadedRecipes:ArrayList<Recipe> = ArrayList()
                for(recipe in document.documents) {
                    var rec = (recipe.toObject(Recipe::class.java)!!)
                    rec.id = recipe.id
                    loadedRecipes.add(rec)
                }
                activity.loadedAllRecipes(loadedRecipes)
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
                activity.showErrorSnackbar("Es konnten keine Rezepte geladen werden. Überprüfe deine Internet Verbindung.")
            }
    }

    fun loadAllRecipesToMainActivity(activity: MainActivity) {
        mFirestore.collection(Constants.RECIPES)
            .get()
            .addOnSuccessListener { document ->
                var loadedRecipes:ArrayList<Recipe> = ArrayList()
                for(recipe in document.documents) {
                    var rec = (recipe.toObject(Recipe::class.java)!!)
                    rec.id = recipe.id
                    loadedRecipes.add(rec)
                }
                activity.loadedAllRecipes(loadedRecipes)
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
                activity.showErrorSnackbar("Es konnten keine Rezepte geladen werden. Überprüfe deine Internet Verbindung.")
            }
    }

}