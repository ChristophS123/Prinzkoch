package de.christoph.prinzkoch

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.christoph.prinzkoch.constants.Constants
import de.christoph.prinzkoch.firebase.FirestoreClass
import de.christoph.prinzkoch.models.Recipe
import de.christoph.prinzkoch.recyclerview.MainRecyclerviewAdapter
import kotlinx.android.synthetic.main.activity_logged_in_main.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.app_bar_logged_in.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_logged_in.*
import kotlinx.android.synthetic.main.nav_header_logged_in.*

class LoggedInMainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var allRecipes:ArrayList<Recipe> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged_in_main)
        setUpActionBar()
        nav_view_logged_in.setNavigationItemSelectedListener(this)
        FirestoreClass().getUserNameAndImageByID(this, FirebaseAuth.getInstance().currentUser!!.uid)
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().loadAllRecipes(this)
    }

    fun loadedAllRecipes(allRecipesLoaded:ArrayList<Recipe>) {
        hideProgressDialog()
        allRecipes = allRecipesLoaded
        setUpRecyclerview()
    }

    private fun setUpRecyclerview() {
        if(allRecipes.size > 0) {
            rv_main_logged_in.visibility = View.VISIBLE
            rv_main_logged_in.layoutManager = LinearLayoutManager(this)
            rv_main_logged_in.setHasFixedSize(true)
            val adapter:MainRecyclerviewAdapter = MainRecyclerviewAdapter(this, allRecipes)
            rv_main_logged_in.adapter = adapter
            adapter.setOnClickListener(object:MainRecyclerviewAdapter.OnClickListener {
                override fun onClick(position: Int, model: Recipe) {
                    var intent:Intent = Intent(this@LoggedInMainActivity, RecipeDetails::class.java)
                    intent.putExtra(Constants.RECIPE_MODEL, model)
                    startActivity(intent)
                }
            })
        }
    }

    fun fillInformationsToNavMenu(name:String, image:String) {
        tv_nav_header_logged_in_name.text = name
        Glide
            .with(this)
            .load(image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(nav_header_logged_in_profile_image)
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_logged_in_activity)
        toolbar_logged_in_activity.setNavigationIcon(R.drawable.ic_action_navigation_menu)
        toolbar_logged_in_activity.setNavigationOnClickListener {
            onNavigationClick()
        }
    }

    private fun onNavigationClick() {
        toggleDrawer()
    }

    override fun onBackPressed() {
        if(logged_in_drawer_layout.isDrawerOpen(GravityCompat.START))
            logged_in_drawer_layout.closeDrawer(GravityCompat.START)
    }

    private fun toggleDrawer() {
        if(logged_in_drawer_layout.isDrawerOpen(GravityCompat.START)) {
            logged_in_drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            logged_in_drawer_layout.openDrawer(GravityCompat.START)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.log_out -> {
                Firebase.auth.signOut()
                startActivity(Intent(this@LoggedInMainActivity, MainActivity::class.java))
            }
            R.id.my_profile -> {
                startActivityForResult(Intent(this, MyProfileActivity::class.java), MY_PROFILE_CODE)
            }
            R.id.new_recipe -> {
                startActivityForResult(Intent(this, CreateNewRecipe::class.java), CREATE_NEW_RECIPE)
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == MY_PROFILE_CODE) {
            FirestoreClass().getUserNameAndImageByID(this, FirebaseAuth.getInstance().currentUser!!.uid)
        } else if(resultCode == Activity.RESULT_OK && requestCode == CREATE_NEW_RECIPE) {
            showProgressDialog(resources.getString(R.string.please_wait))
            FirestoreClass().loadAllRecipes(this)
        }
    }

    companion object {
        const val MY_PROFILE_CODE = 10
        const val CREATE_NEW_RECIPE = 11
    }

}