package com.findmyrecycling

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.findmyrecycling.dto.Product
import com.findmyrecycling.dto.User
import com.findmyrecycling.service.ProductService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings



import kotlinx.coroutines.launch
import org.koin.core.component.getScopeId


class MainViewModel(var productService : ProductService = ProductService()) : ViewModel()  {

    var products : MutableLiveData<List<Product>> = MutableLiveData<List<Product>>()




    var user : User? = null
    private lateinit var firestore : FirebaseFirestore


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

    fun save(product: Product){
        val document = firestore.collection("product").document()
        //product.id = document.id
       var handle =  document.set(product)
        handle.addOnSuccessListener { Log.d("Firebase", "Document Saved") }
    }
    fun saveUser () {
        user?.let {
                user ->
            val handle = firestore.collection("users").document(user.uid).set(user)
            handle.addOnSuccessListener { Log.d("Firebase", "Document Saved") }
            handle.addOnFailureListener { Log.e("Firebase", "Save failed $it ") }
        }
    }

}