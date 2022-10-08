package de.christoph.prinzkoch.firebase

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import de.christoph.prinzkoch.CreateNewRecipe
import de.christoph.prinzkoch.LoggedInMainActivity
import de.christoph.prinzkoch.MyProfileActivity
import de.christoph.prinzkoch.RegisterActivity
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

}