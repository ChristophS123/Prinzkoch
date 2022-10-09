package de.christoph.prinzkoch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import de.christoph.prinzkoch.constants.Constants
import de.christoph.prinzkoch.models.Recipe
import kotlinx.android.synthetic.main.activity_recipe_details.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.item_recipe.*
import kotlinx.android.synthetic.main.item_recipe.view.*

class RecipeDetails : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)
        fillWithData()
        setUpActionBar()
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
            onBackPressed()
        }
    }

}