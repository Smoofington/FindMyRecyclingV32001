package com.findmyrecycling

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    var products: MutableLiveData<List<Product>> = MutableLiveData<List<Product>>()
    val photos: ArrayList<Photo> = ArrayList<Photo>()
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

    fun save(product: Product) {
        val document = firestore.collection("product").document()
        var productID = product.productId.toString()
        productID = document.id
        var handle = document.set(product)
        handle.addOnSuccessListener { Log.d("Firebase", "Document Saved")
        // inserted new code here.
            if (photos.isNotEmpty()){
                uploadPhotos()
            }
        }
    }

    private fun uploadPhotos() {
        photos.forEach{
            photo ->
            var uri = Uri.parse(photo.localUri)
            var imageRef = storageReference.child("images/${user?.uid}/${uri.lastPathSegment}")
            var uploadTask = imageRef.putFile(uri)
            uploadTask.addOnSuccessListener {
                Log.i(TAG, "Image Uploaded $imageRef")
                val downloadUrl = imageRef.downloadUrl
                downloadUrl.addOnSuccessListener {
                    remoteUri ->
                    photo.remoteUri = remoteUri.toString()
                    updatePhotoDatabase(photo)
                }
            }
            uploadTask.addOnFailureListener {
                Log.e(TAG, it.message ?: "No message")
            }
        }
    }

    private fun updatePhotoDatabase(photo: Photo) {
        user?.let {
                user ->
            // it throws an error if I typed selectedProduct.productId in photoCollection and H
            var photoCollection = firestore.collection("users").document(user.uid).collection("products").document(
                selectedProduct?.productId.toString()).collection("photos")

            var handle = photoCollection.add(photo)
            handle.addOnSuccessListener {
                Log.i(TAG, "Successfully updated photo metadata")
                photo.id = it.id
                firestore.collection("users").document(user.uid).collection("products").document(
                    selectedProduct?.productId.toString()).collection("photos").document(photo.id).set(photo)
            }
            handle.addOnFailureListener {
                Log.e(TAG, "Error updating photo data: ${it.message}")
            }
        }
    }


    fun saveUser() {
        user?.let { user ->
            val handle = firestore.collection("users").document(user.uid).set(user)
            handle.addOnSuccessListener { Log.d("Firebase", "Document Saved") }
            handle.addOnFailureListener { Log.e("Firebase", "Save failed $it ") }
        }
    }



}