package de.christoph.prinzkoch.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.christoph.prinzkoch.R
import de.christoph.prinzkoch.models.Comment
import de.christoph.prinzkoch.models.Recipe
import kotlinx.android.synthetic.main.item_comment.view.*
import kotlinx.android.synthetic.main.item_recipe.view.*

class CommentAdapter(private val context: Context, private var comments:ArrayList<Comment>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CommentAdapter.MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_comment,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model:Comment = comments[position]
        if(holder is MyViewHolder) {
            holder.itemView.tv_item_comment_message.text = model.message
            holder.itemView.tv_item_comment_user_name.text = model.createdByName
            Glide
                .with(context)
                .load(model.createdByImage)
                .centerCrop()
                .placeholder(R.drawable.ic_user_place_holder)
                .into(holder.itemView.iv_item_comment_user_image)
        }
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    private class MyViewHolder(view: View): RecyclerView.ViewHolder(view)

}