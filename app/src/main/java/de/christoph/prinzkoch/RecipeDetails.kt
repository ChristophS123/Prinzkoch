package de.christoph.prinzkoch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import de.christoph.prinzkoch.constants.Constants
import de.christoph.prinzkoch.firebase.FirestoreClass
import de.christoph.prinzkoch.models.Recipe
import kotlinx.android.synthetic.main.activity_recipe_details.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.item_recipe.*
import kotlinx.android.synthetic.main.item_recipe.view.*

class RecipeDetails : BaseActivity() {

    private var likers:ArrayList<String> = ArrayList()
    private lateinit var documentID:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)
        fillWithData()
        setUpActionBar()
        iv_recipe_details_heart_liked.setOnClickListener { unLikeRecipe() }
        iv_recipe_details_heart_unliked.setOnClickListener { likeRecipe() }
        cmd_recipe_details_comments.setOnClickListener {
            val intent:Intent = Intent(this, CommentActivity::class.java)
            intent.putExtra(Constants.DOCUMENTID, documentID)
            startActivity(intent)
        }
    }

    private fun likeRecipe() {
        if(FirebaseAuth.getInstance().currentUser != null) {
            likers.add(FirebaseAuth.getInstance().currentUser!!.uid)
            showProgressDialog(resources.getString(R.string.please_wait))
            FirestoreClass().newUserLikedRecipe(this, likers, documentID)
        } else
            showErrorSnackbar("Du musst dich Anmelden, um Rezepte liken zu können.")
    }

    private fun unLikeRecipe() {
        likers.remove(FirebaseAuth.getInstance().currentUser!!.uid)
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().userRemovedLike(this, likers, documentID)
    }

    private fun fillWithData() {
        if(intent.hasExtra(Constants.RECIPE_MODEL)) {
            var model:Recipe = intent.extras!!.get(Constants.RECIPE_MODEL) as Recipe
            Glide
                .with(this)
                .load(model.image)
                .centerCrop()
                .placeholder(R.drawable.ic_recipe_place_holder)
                .into(iv_recipe_details)
            tv_recipe_details_name_of_recipe.text = model.nameOfRecipe
            tv_recipe_details_short_description.text = model.shortDescription
            when(model.category) {
                Constants.BREAKFAST -> tv_recipe_details_category.text = "Frühstück"
                Constants.LUNCH -> tv_recipe_details_category.text = "Mittagessen"
                Constants.DINER -> tv_recipe_details_category.text = "Abendessem"
                Constants.SNACKS -> tv_recipe_details_category.text = "Snacks"
            }
            tv_recipe_details_long_description.text = model.longDescription
            Glide
                .with(this)
                .load(model.creatorImage)
                .centerCrop()
                .placeholder(R.drawable.ic_user_place_holder)
                .into(iv_recipe_details_user_image)
            tv_recipe_details_user_name.text = model.creatorName
            if(FirebaseAuth.getInstance().currentUser != null) {
                if(model.likers.contains(FirebaseAuth.getInstance().currentUser!!.uid)) {
                    iv_recipe_details_heart_liked.visibility = View.VISIBLE
                    iv_recipe_details_heart_unliked.visibility = View.GONE
                } else {
                    iv_recipe_details_heart_liked.visibility = View.GONE
                    iv_recipe_details_heart_unliked.visibility = View.VISIBLE
                }
            } else {
                iv_recipe_details_heart_liked.visibility = View.GONE
                iv_recipe_details_heart_unliked.visibility = View.VISIBLE
            }
            tv_recipe_details_likes.text = model.likers.size.toString()
            likers = model.likers
            documentID = model.id
        } else
            showErrorSnackbar("Das Rezept konnte nicht geladen werden.")
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_recipe_details)
        val actionBar = supportActionBar
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_pressed)
            actionBar.title = tv_recipe_details_name_of_recipe.text.toString()
        }
        toolbar_recipe_details.setNavigationOnClickListener {
            finish()
        }
    }

    fun likedSuccesfully() {
        hideProgressDialog()
        fillWithData()
    }

    fun unLikedSuccesfully() {
        hideProgressDialog()
        fillWithData()
    }

}