package de.christoph.prinzkoch

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import de.christoph.prinzkoch.constants.Constants
import de.christoph.prinzkoch.firebase.FirestoreClass
import de.christoph.prinzkoch.models.Recipe
import de.christoph.prinzkoch.recyclerview.MyRecipeAdapter
import kotlinx.android.synthetic.main.activity_comment.*
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.activity_my_recipes.*

class MyRecipes : BaseActivity() {

    var recipeList:ArrayList<Recipe> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_recipes)
        setUpActionBar()
        getRecipeList()
    }

    private fun getRecipeList() {
        if(intent.hasExtra(Constants.RECIPES)) {
            val allRecipes:ArrayList<Recipe> = intent.extras!!.get(Constants.RECIPES) as ArrayList<Recipe>
            for(i in allRecipes) {
                if(i.creatorID == FirebaseAuth.getInstance().currentUser!!.uid)
                    recipeList.add(i)
            }
            setUpRecyclerView(recipeList)
        }
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_my_recipes_activity)
        val actionBar = supportActionBar
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_pressed)
            actionBar.title = resources.getString(R.string.my_recipes)
        }
        toolbar_my_recipes_activity.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setUpRecyclerView(recipeList: ArrayList<Recipe>) {
        if(recipeList.size > 0) {
            rv_my_recipes_activity.visibility = View.VISIBLE
            rv_my_recipes_activity.layoutManager = LinearLayoutManager(this)
            rv_my_recipes_activity.setHasFixedSize(true)
            val adapter = MyRecipeAdapter(this, recipeList)
            rv_my_recipes_activity.adapter = adapter
            adapter.setOnClickListener(object:MyRecipeAdapter.OnClickListener {
                override fun onClick(position: Int, model: Recipe) {
                    showProgressDialog(resources.getString(R.string.please_wait))
                    recipeList.remove(model)
                    FirestoreClass().deleteRecipe(this@MyRecipes, model.id)
                }
            })
        } else {
            rv_my_recipes_activity.visibility = View.INVISIBLE
            Toast.makeText(this, "Du hast noch keine Rezepte erstellt.", Toast.LENGTH_LONG).show()
        }
    }

    fun recipeSuccesfullyDeleted() {
        hideProgressDialog()
        setUpRecyclerView(recipeList)
    }

}