package de.christoph.prinzkoch.firebase

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import de.christoph.prinzkoch.*
import de.christoph.prinzkoch.constants.Constants
import de.christoph.prinzkoch.models.Comment
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

    fun newUserLikedRecipe(activity: RecipeDetails, likers: java.util.ArrayList<String>, documentID: String) {
        mFirestore.collection(Constants.RECIPES)
            .document(documentID)
            .update(Constants.LIKERS, likers)
            .addOnSuccessListener {
                activity.likedSuccesfully()
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
                activity.showErrorSnackbar("Das Rezept konnte nicht geliked werden. Überprüfe deine Internet Verbindung.")
            }
    }

    fun userRemovedLike(activity: RecipeDetails, likers: java.util.ArrayList<String>, documentID: String) {
        mFirestore.collection(Constants.RECIPES)
            .document(documentID)
            .update(Constants.LIKERS, likers)
            .addOnSuccessListener {
                activity.unLikedSuccesfully()
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
                activity.showErrorSnackbar("Dein Like konnte nicht weggenommen werden. Überprüfe deine Internet Verbindung.")
            }
    }

    fun getUserNameAndImageByIDForComment(activity: CommentActivity, message: String) {
        mFirestore.collection(Constants.USERS)
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .get()
            .addOnSuccessListener { data ->
                val model:User = data.toObject(User::class.java)!!
                activity.addComment(model.id, model.name, model.image, message)
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
                activity.showErrorSnackbar("Dein Kommentar konnte nicht gesendet werden. Überprüfe deine Internet Verbindung.")
            }
    }

    fun saveComment(activity: CommentActivity, comment: Comment) {
        mFirestore.collection(Constants.COMMENTS)
            .document()
            .set(comment)
            .addOnSuccessListener {
                activity.succesfullySavedComment()
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
                activity.showErrorSnackbar("Dein Kommentar konnte nicht gesendet werden. Überprüfe deine Internet Verbindung.")
            }
    }

    fun loadAllComments(activity: CommentActivity, documentID: String) {
        mFirestore.collection(Constants.COMMENTS)
            .whereEqualTo(Constants.RECIPE_COMMENT, documentID)
            .get()
            .addOnSuccessListener { data ->
                if(data.documents.size != 0) {
                    var comments:ArrayList<Comment> = ArrayList()
                    for(i in data.documents) {
                        comments.add(i.toObject(Comment::class.java)!!)
                    }
                    activity.loadedAllComments(comments)
                } else
                    activity.noCommentsExists()
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
                activity.showErrorSnackbar("Kommentare konnten nicht geladen werden. Überprüfe deine Internet Verbindung.")
            }
    }

    fun loadRecipeFromUser(activity: MyRecipes, uid: String) {
        mFirestore.collection(Constants.RECIPES)
            .whereEqualTo(Constants.CREATORID, uid)
            .get()
            .addOnSuccessListener { documents ->
                var recipes:ArrayList<Recipe> = ArrayList()
                for(i in documents.documents) {
                    recipes.add(i.toObject(Recipe::class.java)!!)
                }
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
                activity.showErrorSnackbar("Deine Rezepte konnten nicht geladen werden. Überprüfe deine Internet Verbindung.")
            }
    }

    fun deleteRecipe(activity: MyRecipes, id: String) {
        mFirestore.collection(Constants.RECIPES)
            .document(id)
            .delete()
            .addOnSuccessListener { _ ->
                activity.recipeSuccesfullyDeleted()
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
                activity.showErrorSnackbar("Das Rezept konnte nicht gelöscht werden. Überprüfe deine Internet Verbindung.")
            }
    }

}