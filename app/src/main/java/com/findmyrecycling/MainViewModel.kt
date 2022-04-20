package com.findmyrecycling

import android.content.ContentValues
import android.content.ContentValues.TAG
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
import com.findmyrecycling.service.IProductService
import com.findmyrecycling.service.ProductService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch


class MainViewModel(var productService: IProductService = ProductService()) : ViewModel() {

    val eventPhotos: MutableLiveData<List<Photo>> = MutableLiveData<List<Photo>>()
    val photos: ArrayList<Photo> by mutableStateOf(ArrayList<Photo>())
    val NEW_FACILITY = "New Facility"
    var products: MutableLiveData<List<Product>> = MutableLiveData<List<Product>>()
    var facility: MutableLiveData<List<Facility>> = MutableLiveData<List<Facility>>()
    var selectedFacility by mutableStateOf(Facility())
    var user: User? = null

    private val storageReference = FirebaseStorage.getInstance().getReference()
    private lateinit var firestore: FirebaseFirestore

    init {
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        listenToFacility()
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
                            val facility = it.toObject(Facility::class.java)
                            facility?.let {
                                ALL_FACILITIES.add(it)
                            }
                        }
                        facility.value = ALL_FACILITIES
                    }
                }
        }
    }

    fun fetchProducts() {
        viewModelScope.launch {
            val innerProducts = productService.fetchProducts()
            products.postValue(innerProducts)
        }
    }

    fun saveFacility() {
        // checks to see if facility is already created. If so updates the record, if not then
        // creates a new record
        user?.let { user ->
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

    private fun uploadPhotos() {
        photos.forEach { photo ->
            var uri = Uri.parse(photo.localUri)
            val imageRef = storageReference.child("images/${user?.uid}/${uri.lastPathSegment}")
            val uploadTask = imageRef.putFile(uri)
            uploadTask.addOnSuccessListener {
                Log.i(TAG, "Image Uploaded $imageRef")
                val downloadUrl = imageRef.downloadUrl
                downloadUrl.addOnSuccessListener { remoteUri ->
                    photo.remoteUri = remoteUri.toString()
                    updatePhotoDatabase(photo)
                }
            }
            uploadTask.addOnFailureListener {
                Log.e(TAG, it.message ?: "No message")
            }
        }
    }

    internal fun updatePhotoDatabase(photo: Photo) {
        user?.let { user ->
            var photoDocument = if (photo.id.isEmpty()) {
                // if there is no existing photo create new
                firestore.collection("users").document(user.uid).collection("facilities")
                    .document(selectedFacility.facilityId).collection("photos")
                    .document()
            } else {
                // if there is an existing photo update record
                firestore.collection("users").document(user.uid).collection("facilities")
                    .document(selectedFacility.facilityId).collection("photos")
                    .document(photo.id)
            }
            photo.id = photoDocument.id
            var handle = photoDocument.set(photo)
            handle.addOnSuccessListener {
                Log.i(TAG, "Successfully updated photo metadata")
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

    fun fetchPhotos() {
        user?.let { user ->
            var photoCollection =
                firestore.collection("users").document(user.uid).collection("facilities")
                    .document(selectedFacility.facilityId).collection("photos")
            photoCollection.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                querySnapshot?.let { querySnapshot ->
                    var documents = querySnapshot.documents
                    val inPhotos = ArrayList<Photo>()
                    documents?.forEach {
                        val photo = it.toObject(Photo::class.java)
                        photo?.let { photo ->
                            inPhotos.add(photo)
                        }
                    }
                    eventPhotos.value = inPhotos
                }
            }
        }
    }

    fun delete(photo: Photo) {
        user?.let { user ->
            val photoDocument =
                firestore.collection("users").document(user.uid).collection("facilities")
                    .document(selectedFacility.facilityId).collection("photos")
            photoDocument.document(photo.id).delete()
            val uri = Uri.parse(photo.localUri)
            val imageRef = storageReference.child("images/${user.uid}/${uri.lastPathSegment}")
            imageRef.delete()
                .addOnSuccessListener {
                    Log.i(TAG, "Photo binary file deleted ${photo}")
                }
                .addOnFailureListener {
                    Log.e(TAG, "Photo delete failed. ${it.message}")
                }
        }
    }

    fun clearPhotos() {
        photos.clear()
    }

}