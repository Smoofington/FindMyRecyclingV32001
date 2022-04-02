package com.findmyrecycling

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.findmyrecycling.dto.Facility
import com.findmyrecycling.dto.Product
import com.findmyrecycling.service.ProductService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.coroutines.launch


class MainViewModel(var productService : ProductService = ProductService()) : ViewModel()  {

    var products : MutableLiveData<List<Product>> = MutableLiveData<List<Product>>()
    var facilities : MutableLiveData<List<Facility>> = MutableLiveData<List<Facility>>()

    private lateinit var firestore : FierbaseFirestore

    init {
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        listenToFacilties()
    }

    private fun listenToFacilties() {
        firebase.collection("locations").addSnapshotListener {
            snapshot, e ->
            if (e != null) {
                Log.w("Listen failed", e)
                return@addSnapshotListener
            }
            snapshot?.let{
                val allFacilites = ArrayList<Facility>()
                val documents = snapshot.documents
                documents.forEach {
                    val facility = it.toObject(Facility::class.java)
                    allFacilities.add(facility)
                }
            }
        }
    }

    fun fetchProducts() {
        viewModelScope.launch {
            var innerProducts = productService.fetchProducts()
            products.postValue(innerProducts)
        }
    }

}