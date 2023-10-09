package com.anthony.explore24

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class DetailAdapter(private val context: Context, private val listItem: ArrayList<DetailData>) :
     RecyclerView.Adapter<DetailAdapter.MyViewHolder>() {
     
     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
          val itemView = LayoutInflater
               .from(parent.context)
               .inflate(R.layout.row_detail, parent, false)
          return MyViewHolder(itemView)
     }
     
     override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
          val currentItem = listItem[position]
          Glide
               .with(context)
               .load(currentItem.image)
               .into(holder.images)
     }
     
     override fun getItemCount(): Int {
          return listItem.size
     }
     
     class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
          val images: ImageView = itemView.findViewById(R.id.img_list)
     }
}