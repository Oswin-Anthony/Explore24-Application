package com.anthony.explore24

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ListAdapter(private val context: Context, private val itemList: ArrayList<ListData>) :
     RecyclerView.Adapter<ListAdapter.MyViewHolder>() {
     
     private lateinit var mListener: OnItemClickListener
     
     interface OnItemClickListener {
          fun onItemClick(position: Int)
     }
     
     fun setOnItemClickListener(listener: OnItemClickListener) {
          mListener = listener
     }
     
     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
          val itemView =
               LayoutInflater.from(parent.context).inflate(R.layout.row_list, parent, false)
          return MyViewHolder(itemView, mListener)
     }
     
     override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
          val currentItem = itemList[position]
          Glide
               .with(context)
               .load(currentItem.image)
               .into(holder.image)
          holder.name.text = currentItem.name
          holder.type.text = currentItem.type
          holder.distance.text = currentItem.distance
          holder.rating.text = currentItem.rating.toString()
     }
     
     override fun getItemCount(): Int {
          return itemList.size
     }
     
     class MyViewHolder(itemView: View, listener: OnItemClickListener) :
          RecyclerView.ViewHolder(itemView) {
          val image: ImageView = itemView.findViewById(R.id.img_list)
          val name: TextView = itemView.findViewById(R.id.txt_name)
          val type: TextView = itemView.findViewById(R.id.txt_type)
          val distance: TextView = itemView.findViewById(R.id.txt_distance)
          val rating: TextView = itemView.findViewById(R.id.txt_rating)
          
          init {
               itemView.setOnClickListener {
                    listener.onItemClick(adapterPosition)
               }
          }
     }
}