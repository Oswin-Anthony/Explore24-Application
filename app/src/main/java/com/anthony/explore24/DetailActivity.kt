package com.anthony.explore24

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore


@Suppress("UNCHECKED_CAST")
class DetailActivity : AppCompatActivity() {
     private lateinit var txtName: TextView
     private lateinit var txtType: TextView
     private lateinit var txtRating: TextView
     private lateinit var txtDetails: TextView
     private lateinit var recyclerView: RecyclerView
     private lateinit var arrayList: ArrayList<DetailData>
     private lateinit var images: ArrayList<String>
     private lateinit var adapter: DetailAdapter
     private lateinit var db: FirebaseFirestore
     private lateinit var btnNavigation: Button
     
     @SuppressLint("NotifyDataSetChanged")
     override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)
          setContentView(R.layout.activity_detail)
          
          txtName = findViewById(R.id.txt_name)
          txtType = findViewById(R.id.txt_type)
          txtRating = findViewById(R.id.txt_rating)
          txtDetails = findViewById(R.id.txt_details)
          recyclerView = findViewById(R.id.recycler_view_top)
          btnNavigation = findViewById(R.id.btn_navigate)
          
          val str = intent.getStringExtra("name")
          var url = ""
          txtName.text = str
          txtType.text = intent.getStringExtra("type")
          txtRating.text = intent.getStringExtra("rating")
          txtDetails.text = intent.getStringExtra("details")
          
          recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
          recyclerView.setHasFixedSize(true)
          arrayList = arrayListOf()
          
          db = FirebaseFirestore.getInstance()
          db.collection("list-data").get()
               .addOnSuccessListener {documents ->
                    for (doc in documents) {
                         if (str == doc.get("name")) {
                              images = doc.get("details_img") as ArrayList<String>
                              url = doc.get("link") as String
                         }
                    }
                    images.forEach{
                         arrayList.add(DetailData(it))
                    }
                    recyclerView.adapter!!.notifyDataSetChanged()
               }
               .addOnFailureListener{
                    Toast.makeText(this, "Error while downloading data", Toast.LENGTH_LONG)
                         .show()
               }
          
          adapter = DetailAdapter(this, arrayList)
          recyclerView.adapter = adapter
          
          btnNavigation.setOnClickListener{
               val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
               startActivity(browserIntent)
          }
     }
}