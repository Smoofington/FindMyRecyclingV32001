package com.findmyrecycling

import android.content.ContentValues
import android.net.Uri
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
    internal val NEW_FACILITY = "New Facility"
    var products: MutableLiveData<List<Product>> = MutableLiveData<List<Product>>()
    var facility: MutableLiveData<List<Facility>> = MutableLiveData<List<Facility>>()
    var selectedFacility by mutableStateOf(Facility())
    var user: User? = null

    private lateinit var firestore: FirebaseFirestore
    private val storageReference = FirebaseStorage.getInstance().getReference()


    init {
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    fun fetchProducts() {
        viewModelScope.launch {
            val innerProducts = productService.fetchProducts()
            products.postValue(innerProducts)
        }
    }

    private fun uploadPhotos() {
        photos.forEach {
                photo ->
            var uri = Uri.parse(photo.localUri)
            val imageRef = storageReference.child("images/${user?.uid}/${uri.lastPathSegment}")
            val uploadTask  = imageRef.putFile(uri)
            uploadTask.addOnSuccessListener {
                Log.i(ContentValues.TAG, "Image Uploaded $imageRef")
                val downloadUrl = imageRef.downloadUrl
                downloadUrl.addOnSuccessListener {
                        remoteUri ->
                    photo.remoteUri = remoteUri.toString()
                    updatePhotoDatabase(photo)

                }
            }
            uploadTask.addOnFailureListener {
                Log.e(ContentValues.TAG, it.message ?: "No message")
            }
        }
    }
    private fun updatePhotoDatabase(photo: Photo) {
        user?.let {
                user ->
            var photoCollection = firestore.collection("users").document(user.uid).collection("facilities").document(
                selectedFacility?.facilityId.toString()
            ).collection("photos")
            var handle = photoCollection.add(photo)
            handle.addOnSuccessListener {
                Log.i(ContentValues.TAG, "Successfully updated photo metadata")
                photo.id = it.id
                firestore.collection("users").document(user.uid).collection("facilities")
                    .document(selectedFacility.facilityId).collection("photos").document(photo.id)
                    .set(photo)
            }
            handle.addOnFailureListener {
                Log.e(ContentValues.TAG, "Error updating photo data: ${it.message}")
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

    fun listenToFacility() {
        user?.let { user ->
            firestore.collection("users").document(user.uid).collection("facilities")
                .addSnapshotListener { snapshot, e ->
                    // handle the error if there is one, and the return
                    if (e != null) {
                        Log.w("Listen failed", e)
                        return@addSnapshotListener
                    }
                    // if we reached this point, there was not an error
                    snapshot?.let {
                        val ALL_FACILITIES = ArrayList<Facility>()
                        ALL_FACILITIES.add(Facility(facilityName = NEW_FACILITY))
                        val DOCUMENTS = snapshot.documents
                        DOCUMENTS.forEach {
                            var facility = it.toObject(Facility::class.java)
                            facility?.let {
                                ALL_FACILITIES.add(it)
                            }
                        }
                        facility.value = ALL_FACILITIES
                    }
                }
        }
    }

    fun saveFacility() {
        // checks to see if facility is already created. If so updates the record, if not then
        // creates a new record
        user?.let {
            user ->
                val document =
                    if (selectedFacility.facilityId == null || selectedFacility.facilityId.isEmpty()) {
                        firestore.collection("users").document(user.uid).collection("facilities")
                            .document()
                    } else {
                        firestore.collection("users").document(user.uid).collection("facilities")
                            .document(selectedFacility.facilityId)
                    }
                selectedFacility.facilityId = document.id
                val handle = document.set(selectedFacility)
                handle.addOnSuccessListener {
                    Log.d("Firebase", "Document Saved")
                    if (photos.isNotEmpty()) {
                        uploadPhotos()
                    }
                }
                handle.addOnFailureListener { Log.e("Firebase", "Save failed $it ") }
        }
    }

}