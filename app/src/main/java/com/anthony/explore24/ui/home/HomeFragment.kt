package com.anthony.explore24.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anthony.explore24.DetailActivity
import com.anthony.explore24.ListData
import com.anthony.explore24.R
import com.anthony.explore24.databinding.FragmentHomeBinding
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {
     private var _binding: FragmentHomeBinding? = null
     private val binding get() = _binding!!
     private lateinit var adapterV: HomeAdapter
     private lateinit var recyclerViewV: RecyclerView
     private lateinit var arrayListV: ArrayList<ListData>
     private lateinit var txtDetails: ArrayList<String>
     private lateinit var image: ArrayList<String>
     private lateinit var txtName: ArrayList<String>
     private lateinit var txtType: ArrayList<String>
     private lateinit var txtDistance: ArrayList<String>
     private lateinit var txtRating: ArrayList<Double>
     private lateinit var db: FirebaseFirestore
     
     override fun onCreateView(
          inflater: LayoutInflater,
          container: ViewGroup?,
          savedInstanceState: Bundle?
     ): View {
          _binding = FragmentHomeBinding.inflate(inflater, container, false)
          return binding.root
     }
     
     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
          super.onViewCreated(view, savedInstanceState)
          
          dataInitialize()
          
          val layoutManagerV = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
          recyclerViewV = view.findViewById(R.id.home_list)
          recyclerViewV.layoutManager = layoutManagerV
          recyclerViewV.setHasFixedSize(true)
          adapterV = HomeAdapter(requireContext(), arrayListV)
          recyclerViewV.adapter = adapterV
          adapterV.setOnItemClickListener(object : HomeAdapter.OnItemClickListener {
               override fun onItemClick(position: Int) {
                    val intent = Intent(context, DetailActivity::class.java)
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
          arrayListV = arrayListOf()
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
                    for (i in image.indices) {
                         val data = ListData(
                              image[i],
                              txtName[i],
                              txtType[i],
                              txtDistance[i],
                              txtRating[i]
                         )
                         arrayListV.add(data)
                    }
                    recyclerViewV.adapter!!.notifyDataSetChanged()
               }
               .addOnFailureListener {
                    Toast
                         .makeText(
                              context,
                              "Error while downloading data",
                              Toast.LENGTH_LONG
                         )
                         .show()
               }
     }
     
     override fun onDestroyView() {
          super.onDestroyView()
          _binding = null
     }
}