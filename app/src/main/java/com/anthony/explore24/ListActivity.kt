package com.anthony.explore24

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

class ListActivity : AppCompatActivity() {
     private lateinit var recyclerView: RecyclerView
     private lateinit var adapter: ListAdapter
     private lateinit var arrayList: ArrayList<ListData>
     private lateinit var txtDetails: ArrayList<String>
     private lateinit var image: ArrayList<String>
     private lateinit var txtName: ArrayList<String>
     private lateinit var txtType: ArrayList<String>
     private lateinit var txtDistance: ArrayList<String>
     private lateinit var txtRating: ArrayList<Double>
     private lateinit var db: FirebaseFirestore
     private lateinit var searchView: SearchView
     private lateinit var searchList: ArrayList<ListData>
     private var des: String = ""
     
     override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)
          setContentView(R.layout.activity_list)
          
          des = intent.getStringExtra("description")!!
          
          dataInitialize()
          
          recyclerView = findViewById(R.id.recycler_view_top)
          recyclerView.layoutManager = LinearLayoutManager(this)
          recyclerView.setHasFixedSize(true)
          
          searchView = findViewById(R.id.search_view)
          searchView.clearFocus()
          searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
               override fun onQueryTextSubmit(query: String?): Boolean {
                    searchView.clearFocus()
                    return true
               }
               
               @SuppressLint("NotifyDataSetChanged")
               override fun onQueryTextChange(newText: String?): Boolean {
                    searchList.clear()
                    val searchText = newText!!.lowercase(Locale.getDefault())
                    if (searchText.isNotEmpty()) {
                         arrayList.forEach {
                              if (it.name.lowercase(Locale.getDefault()).contains(searchText) ||
                                   it.type.lowercase(Locale.getDefault()).contains(searchText)
                              ) {
                                   searchList.add(it)
                              }
                         }
                    } else {
                         searchList.clear()
                         searchList.addAll(arrayList)
                    }
                    recyclerView.adapter!!.notifyDataSetChanged()
                    return false
               }
          })
          
          adapter = ListAdapter(this, searchList)
          recyclerView.adapter = adapter
          adapter.setOnItemClickListener(object : ListAdapter.OnItemClickListener {
               override fun onItemClick(position: Int) {
                    val intent = Intent(this@ListActivity, DetailActivity::class.java)
                    intent.putExtra("name", txtName[position])
                    intent.putExtra("type", txtType[position])
                    intent.putExtra("rating", txtRating[position].toString())
                    intent.putExtra("details", txtDetails[position])
                    startActivity(intent)
               }
          })
     }
     
     @SuppressLint("NotifyDataSetChanged")
     private fun dataInitialize() {
          arrayList = arrayListOf()
          searchList = arrayListOf()
          
          image = arrayListOf()
          txtName = arrayListOf()
          txtType = arrayListOf()
          txtDistance = arrayListOf()
          txtRating = arrayListOf()
          txtDetails = arrayListOf()
          
          db = FirebaseFirestore.getInstance()
          db.collection("list-data").get()
               .addOnSuccessListener { documents ->
                    for (doc in documents) {
                         txtDetails.add(doc.get("details") as String)
                         image.add(doc.get("image") as String)
                         txtName.add(doc.get("name") as String)
                         txtType.add(doc.get("type") as String)
                         txtRating.add(doc.get("rating") as Double)
                         txtDistance.add("100m")
                    }
                    if(des == "all") {
                         for (i in image.indices) {
                              val data = ListData(
                                   image[i],
                                   txtName[i],
                                   txtType[i],
                                   txtDistance[i],
                                   txtRating[i]
                              )
                              arrayList.add(data)
                              searchList.add(data)
                         }
                    } else {
                         for (i in image.indices) {
                              if (txtType[i].lowercase() == des.lowercase()) {
                                   val data = ListData(
                                        image[i],
                                        txtName[i],
                                        txtType[i],
                                        txtDistance[i],
                                        txtRating[i]
                                   )
                                   arrayList.add(data)
                                   searchList.add(data)
                              }
                         }
                    }
                    recyclerView.adapter!!.notifyDataSetChanged()
               }
               .addOnFailureListener {
                    Toast
                         .makeText(this, "Error while downloading data", Toast.LENGTH_LONG)
                         .show()
               }
     }
}