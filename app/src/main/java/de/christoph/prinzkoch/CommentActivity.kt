package de.christoph.prinzkoch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import de.christoph.prinzkoch.constants.Constants
import de.christoph.prinzkoch.firebase.FirestoreClass
import de.christoph.prinzkoch.models.Comment
import de.christoph.prinzkoch.recyclerview.CommentAdapter
import kotlinx.android.synthetic.main.activity_comment.*
import kotlinx.android.synthetic.main.activity_recipe_details.*

class CommentActivity : BaseActivity() {

    private lateinit var documentID:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)
        setUpActionBar()
        getDocumentID()
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().loadAllComments(this, documentID)
        cmd_add_comment.setOnClickListener { onAddCommentButtonClick() }
    }

    private fun getDocumentID() {
        if(intent.hasExtra(Constants.DOCUMENTID)) {
            documentID = intent.getStringExtra(Constants.DOCUMENTID).toString()
        } else
            documentID = ""
    }

    private fun onAddCommentButtonClick() {
        if(et_comment_activity.text.toString().isNotEmpty()) {
            val message:String = et_comment_activity.text!!.toString()
            if(FirebaseAuth.getInstance().currentUser != null) {
                showProgressDialog(resources.getString(R.string.please_wait))
                FirestoreClass().getUserNameAndImageByIDForComment(this, message)
            } else
                showErrorSnackbar("Du musst dich anmelden, um zu kommentieren.")
        } else
            showErrorSnackbar("Bitte gebe eine Nachricht ein.")
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_comment_activity)
        val actionBar = supportActionBar
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_pressed)
            actionBar.title = resources.getString(R.string.comment)
        }
        toolbar_comment_activity.setNavigationOnClickListener {
            finish()
        }
    }

    fun addComment(userID: String, userName: String, userImage: String, message: String) {
        val comment:Comment = Comment(userID, userName, userImage, documentID, message)
        FirestoreClass().saveComment(this, comment)
    }

    fun succesfullySavedComment() {
        hideProgressDialog()
        et_comment_activity.setText("")
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().loadAllComments(this, documentID)
    }

    fun noCommentsExists() {
        hideProgressDialog()
        Toast.makeText(this, "Dieses Rezept wurde noch nie kommentiert.", Toast.LENGTH_LONG).show()
    }

    fun loadedAllComments(comments: ArrayList<Comment>) {
        hideProgressDialog()
        setUpRecyclerView(comments)
    }

    private fun setUpRecyclerView(comments:ArrayList<Comment>) {
        rv_comment_activity.visibility = View.VISIBLE
        rv_comment_activity.layoutManager = LinearLayoutManager(this)
        rv_comment_activity.setHasFixedSize(true)
        val adapter = CommentAdapter(this, comments)
        rv_comment_activity.adapter = adapter
    }

}