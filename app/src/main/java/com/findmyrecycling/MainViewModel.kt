package com.findmyrecycling

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.findmyrecycling.dto.Facility
import com.findmyrecycling.dto.Photo
import com.findmyrecycling.dto.Product
import com.findmyrecycling.dto.User
import com.findmyrecycling.service.ProductService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import org.koin.core.component.getScopeId


class MainViewModel(var productService: ProductService = ProductService()) : ViewModel() {

    val photos: ArrayList<Photo> = ArrayList<Photo>()
    internal val NEW_PRODUCT = "New Product"
    var products: MutableLiveData<List<Product>> = MutableLiveData<List<Product>>()
    var facility: MutableLiveData<List<Facility>> = MutableLiveData<List<Facility>>()
    var selectedProdcut by mutableStateOf(Product())
    var user: User? = null

    private lateinit var firestore: FirebaseFirestore
    private var storageReference = FirebaseStorage.getInstance().getReference()


    init {
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    fun fetchProducts() {
        viewModelScope.launch {
            var innerProducts = productService.fetchProducts()
            products.postValue(innerProducts)
        }
    }

    fun save(facility: Facility) {
        val document = firestore.collection("facility").document()
        facility.facilityId = document.id
        var handle = document.set(facility)
        handle.addOnSuccessListener { Log.d("Firebase", "Document Saved") }
    }

    fun saveUser() {
        user?.let { user ->
            val handle = firestore.collection("users").document(user.uid).set(user)
            handle.addOnSuccessListener { Log.d("Firebase", "Document Saved") }
            handle.addOnFailureListener { Log.e("Firebase", "Save failed $it ") }
        }
    }

}