package com.findmyrecycling

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.findmyrecycling.dto.Facility
import com.findmyrecycling.dto.Photo
import com.findmyrecycling.dto.User
import com.findmyrecycling.ui.theme.FindMyRecyclingTheme
import com.findmyrecycling.ui.theme.RecyclingBlue
import com.findmyrecycling.ui.theme.RecyclingGray
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FacebookAuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ProfileScreen : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel<MainViewModel>()
    private var uri: Uri? = null
    private lateinit var currentImagePath: String
    private var strUri by mutableStateOf("")
    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            firebaseUser?.let {
                val user = User(it.uid, "")
                viewModel.user = user
                viewModel.listenToFacility()
            }
            FindMyRecyclingTheme {
                Surface(
                    color = MaterialTheme.colors.background,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ProfileOptions("Android", viewModel.selectedFacility)
                }
            }
        }
    }


    @Composable
    fun MainMenu() {
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            Icon(imageVector = Icons.Filled.Menu, contentDescription = "")
        }
    }

    @Composable
    fun ProfileOptions(
        name: String,
        selectedFacility: Facility = Facility()
        ) {
        var inFacilityName by remember(selectedFacility.facilityId) { mutableStateOf(selectedFacility.facilityName) }
        var inFacilityLocation by remember(selectedFacility.location) { mutableStateOf(selectedFacility.location) }
        var inFacilityDescription by remember(selectedFacility.description) { mutableStateOf(selectedFacility.description) }
        var inRecyclableProduct by remember(selectedFacility.recyclableProducts) { mutableStateOf(selectedFacility.recyclableProducts) }
        val context = LocalContext.current
        Column {
            OutlinedTextField(
                value = inFacilityName,
                onValueChange = { inFacilityName = it },
                label = { Text(stringResource(R.string.facilityName), fontSize = 17.sp, fontWeight = FontWeight.W800) },
                modifier = Modifier.fillMaxWidth()
                    .background(color = RecyclingGray)
            )
            OutlinedTextField(
                value = inFacilityLocation,
                onValueChange = { inFacilityLocation = it },
                label = { Text(stringResource(R.string.facilityLocation), fontSize = 17.sp, fontWeight = FontWeight.W800) },
                modifier = Modifier.fillMaxWidth()
                    .background(color = RecyclingGray)
            )
            OutlinedTextField(
                value = inFacilityDescription,
                onValueChange = { inFacilityDescription = it },
                label = { Text(stringResource(R.string.facilityDetails), fontSize = 17.sp, fontWeight = FontWeight.W800) },
                modifier = Modifier.fillMaxWidth()
                    .background(color = RecyclingGray)
            )
            OutlinedTextField(
                value = inRecyclableProduct,
                onValueChange = { inRecyclableProduct = it },
                label = { Text(stringResource(R.string.recyclableProduct), fontSize = 17.sp, fontWeight = FontWeight.W800) },
                modifier = Modifier.fillMaxWidth()
                    .background(color = RecyclingGray)
            )
            Row {
                Button(
                    onClick = {
                        selectedFacility.apply {
                            facilityName = inFacilityName
                            location = inFacilityLocation
                            description = inFacilityDescription
                            recyclableProducts = inRecyclableProduct
                        }
                        viewModel.saveFacility()
                        Toast.makeText(
                            context,
                            "$inFacilityName $inFacilityLocation $inFacilityDescription",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                ) {
                    Text(text = "Save", color = RecyclingBlue, fontSize = 17.sp)
                }
                Button(
                    onClick = {
                        takePhoto()
                    }
                )
                {
                    Text(text = "Photo")
                }
                Button(
                    onClick = {
                        signIn()
                    }
                )
                {
                    Text(text = "Log On")
                }
            }
            AsyncImage(model = strUri, contentDescription = "Facility image")
        }
    }
    private fun takePhoto() {
        if (hasCameraPermission() == PackageManager.PERMISSION_GRANTED && hasExternalStoragePermission() == PackageManager.PERMISSION_GRANTED) {
            // The user has already granted permission for these activities.  Toggle the camera!
            invokeCamera()
        } else {
            // The user has not granted permissions, so we must request.
            requestMultiplePermissionsLauncher.launch(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,

                    )
            )
        }
    }
    private val requestMultiplePermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultsMap ->
        var permissionGranted = false
        resultsMap.forEach {
            if (it.value == true) {
                permissionGranted = it.value
            } else {
                permissionGranted = false
                return@forEach
            }
        }
        if (permissionGranted) {
            invokeCamera()
        } else {
            Toast.makeText(this, getString(R.string.cameraPermissionDenied), Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun invokeCamera() {
        val file = createImageFile()
        try {
            uri = FileProvider.getUriForFile(this, "com.findmyrecycling.fileprovider", file)
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "Error: ${e.message}")
            var foo = e.message
        }
        getCameraImage.launch(uri)
    }

    private fun createImageFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "Facility_${timestamp}",
            ".jpg",
            imageDirectory
        ).apply {
            currentImagePath = absolutePath
        }
    }

    private val getCameraImage =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                Log.i(ContentValues.TAG, "Image Location: $uri")
                strUri = uri.toString()
                val photo = Photo(localUri = uri.toString())
                viewModel.photos.add(photo)
            } else {
                Log.e(ContentValues.TAG, "Image not saved. $uri")
            }

        }

    private fun hasCameraPermission() = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
    private fun hasExternalStoragePermission() =
        ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private fun signIn() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res -> this.signInResult(res) }


    private fun signInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            firebaseUser = FirebaseAuth.getInstance().currentUser
            firebaseUser?.let {
                val user = User(it.uid, it.displayName)
                viewModel.user = user
                viewModel.saveUser()
                viewModel.listenToFacility()
            }
        } else {
            Log.e("MainActivity.kt", "Error logging in " + response?.error?.errorCode)
        }

    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview2() {
        FindMyRecyclingTheme {
            ProfileOptions("Android")
        }
    }
}