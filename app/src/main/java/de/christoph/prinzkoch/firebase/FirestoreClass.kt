package de.christoph.prinzkoch.firebase

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import de.christoph.prinzkoch.RegisterActivity
import de.christoph.prinzkoch.constants.Constants
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

}