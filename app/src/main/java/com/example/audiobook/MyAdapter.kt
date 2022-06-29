package com.example.audiobook

import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MyAdapter(private val bookList : ArrayList<Book>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    private lateinit var mListner : onItemClickListner
    interface onItemClickListner{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListner(listner: onItemClickListner){
        mListner = listner
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.itemview,
            parent,false)
        return ViewHolder(itemView,mListner)
    }

    override fun onBindViewHolder(holder: MyAdapter.ViewHolder, position: Int) {
        val currentItem = bookList[position]

        holder.title.text = currentItem.title
        holder.writer.text = currentItem.writer
        Glide.with(holder.image.context).load(currentItem.image).into(holder.image)
        Log.d(TAG, "onBindViewHolder: "+currentItem.image)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    class ViewHolder(itemView: View,listner: onItemClickListner) : RecyclerView.ViewHolder(itemView) {
        val title : TextView = itemView.findViewById(R.id.bookName)
        val writer : TextView = itemView.findViewById(R.id.book_writer)
        val image : ImageView = itemView.findViewById(R.id.bookImage)

        init {
            itemView.setOnClickListener {
                listner.onItemClick(adapterPosition)
            }
        }
    }


}