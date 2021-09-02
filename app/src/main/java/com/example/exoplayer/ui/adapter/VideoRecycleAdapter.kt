package com.example.exoplayer.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exoplayer.R
import com.example.exoplayer.model.Videos
import com.example.exoplayer.ui.activities.ExoPlayerActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_blog_list_item.view.*


class VideoRecycleAdapter(private val listener: OnItemClickListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{

    private var items: List<Videos> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return VideoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_blog_list_item,
                parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.title.text= items[position].title
        holder.itemView.subtitle.text= items[position].subtitle
        val targetImageView = holder.itemView.thumb
        Picasso.get().load(items[position].thumb)
            .into(targetImageView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(videoList: List<Videos>){
        items = videoList
    }

    inner class VideoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),
        View.OnClickListener{

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position:Int = absoluteAdapterPosition
            if(position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
                val intent = Intent(v?.context, ExoPlayerActivity::class.java)
                intent.putExtra("id",position.toString())
                v?.context?.startActivity(intent)

            }
        }
    }
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

}
