package com.findmyrecycling

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModel
import coil.compose.AsyncImage
import com.findmyrecycling.dto.Photo
import com.findmyrecycling.dto.Product
import com.findmyrecycling.ui.theme.FindMyRecyclingTheme
import com.google.common.collect.Collections2.filter
import com.google.common.collect.Iterables.filter
import com.google.common.collect.Iterators.filter
import com.google.common.collect.Multisets.filter
import com.google.common.collect.Sets.filter
import java.util.Locale.filter
import com.findmyrecycling.ui.theme.FindMyRecyclingTheme
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import org.koin.androidx.viewmodel.ext.android.viewModel

var selectedProduct: Product? = null
private var uri: Uri? = null
private lateinit var currentImagePath: String
private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
private val viewModel: MainViewModel by viewModel<MainViewModel>()
private var inProductName: String = ""
private var strUri by mutableStateOf("")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
           // MainViewModel.FetchProducts()
            val products = ArrayList<Product>()
            products.add(Product(product = "Tin Can", productId = 0))
            products.add(Product(product = "Car Door", productId = 1))
            products.add(Product(product = "Glass", productId = 2))
            FindMyRecyclingTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background,
                modifier = Modifier.fillMaxWidth()) {
                    RecycleSearch("Android", products)
                }
            }
        }
    }

    @Composable
    fun ProductSpinner (products: List<Product>){
        var productText by remember {mutableStateOf("Product List")}
        var expanded by remember {mutableStateOf(false)}
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){

            Row(Modifier
                .padding(24.dp)
                .clickable {
                    expanded = !expanded
                }
                .padding(8.dp),
                horizontalArrangement= Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = productText,fontSize =18.sp, modifier = Modifier.padding(end =8.dp))
                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "")
                DropdownMenu(expanded = expanded, onDismissRequest = {expanded = false }) {
                    products.forEach{
                        product -> DropdownMenuItem(onClick = {
                          expanded = false
                        productText = product.toString()
                        selectedProduct = product
                    }) {
                            Text(text = product.toString())
                    }
                    }
                }
            }
        }
    }

    @Composable
    fun ProfileMenu(){
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            Icon(imageVector = Icons.Filled.Menu, contentDescription = "")

        }
    }

    @Composable
    fun TextFieldWithDropdownUsage(data: List<Product>){
        val dropDownOptions = remember{ mutableStateOf(listOf<Product>())}
        val textFieldValue = remember {mutableStateOf(TextFieldValue())}
        val dropDownExpanded = remember {mutableStateOf(false)}

        fun onDropdownDismissRequest(){
            dropDownExpanded.value = false
        }

        fun onValueChanged(value: TextFieldValue){
            //strSelectedData = value.text
            dropDownExpanded.value = true
            textFieldValue.value = value
           // dropDownOptions.value =ProductIn.filter { it.toString().startsWith(value.text) && it.toString()}
        }
    }

    @Composable
    fun TextFieldWithDropdown(
        modifier: Modifier = Modifier,
        value: TextFieldValue,
        setValue: (TextFieldValue) -> Unit,
        onDismissRequest: () -> Unit,
        dropDownExpanded: Boolean,
        list: List<Product>,
        label: String = ""
    ){}




    @Composable
    fun RecycleSearch(name: String, products: List<Product> = ArrayList<Product>()) {
        var recyclable by remember{ mutableStateOf("")}
        var location by remember{ mutableStateOf("")}
        val context = LocalContext.current
       // val i = ImageView(this).apply {
       //     setImageResource(R.drawable.ic_hamburger_menu.)
      //  }
        //var hamburger_Menu = ImageView(this).apply{
        //    setImageResource(R.drawable.ic_hamburger_menu)
        //}
        Row {
            Arrangement.Center
            ProfileMenu()
        }

        Column {

            ProductSpinner(products = products)
            //TextFieldWithDropdownUsage(dataIn = pr)

            OutlinedTextField(
                value = recyclable,
                onValueChange = {recyclable = it},
                label = { Text(stringResource(R.string.recyclable)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 4.dp, top = 2.dp, bottom = 2.dp)
                    .border(width = 1.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(4.dp)),
            )

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text(stringResource(R.string.location)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 4.dp, top = 2.dp, bottom = 2.dp)
                    .border(width = 1.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(4.dp)),
            )
            Row(modifier = Modifier.padding(all = 2.dp)) {
                Button(
                    onClick = {
                        // Toast.makeText(context, "$name")
                    }
                )
                {
                    Text(text = "Save")
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
                        addFacility()
                    }
                )
                {
                    Text(text = "Add Facility")
                }
            }
            AsyncImage(model = strUri, contentDescription= "Facility and/or Product image")
        }
    }


    private fun takePhoto() {
        if (hasCameraPermission() == PackageManager.PERMISSION_GRANTED && hasExternalStoragePermission() == PackageManager.PERMISSION_GRANTED) {
            // The user has already granted permission for these activities.  Toggle the camera!
            invokeCamera()
        } else {
            // The user has not granted permissions, so we must request.
            requestMultiplePermissionsLauncher.launch(arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ))
        }
    }

    private val requestMultiplePermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) {
            resultsMap ->
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
            Toast.makeText(this, "Unable to load camera without permission.", Toast.LENGTH_LONG).show()
        }
    }

    private fun invokeCamera() {
        val file = createImageFile()
        try {
            uri = FileProvider.getUriForFile(this, "app.plantdiary.fileprovider", file)
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "Error: ${e.message}")
            var foo = e.message
        }
        getCameraImage.launch(uri)
    }

    private fun createImageFile() : File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "Specimen_${timestamp}",
            ".jpg",
            imageDirectory
        ).apply {
            currentImagePath = absolutePath
        }
    }

    private val getCameraImage = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            success ->
        if (success) {
            Log.i(ContentValues.TAG, "Image Location: $uri")
            strUri = uri.toString()
            val photo = Photo(localUri = uri.toString())
            viewModel.photos.add(photo)
        } else {
            Log.e(ContentValues.TAG, "Image not saved. $uri")
        }

    }

    fun hasCameraPermission() = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
    fun hasExternalStoragePermission() = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        FindMyRecyclingTheme {
            RecycleSearch("Android")
        }

        Button(
            onClick = {
                signIn()
            }
        ) {
            Text(text = "Logon")
        }
    }

    fun signIn() {
        val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())
        val signinIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()

        signInLauncher.launch(signinIntent)
    }

    private val signInLauncher = registerForActivityResult (
        FirebaseAuthUIActivityResultContract()
    ) {
            res -> this.signInResult(res)
    }


    private fun signInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == ComponentActivity.RESULT_OK) {
            var user = FirebaseAuth.getInstance().currentUser
        }
    }

    fun addFacility() {
        val intent = Intent(this, ProfileScreen::class.java)
        startActivity(intent)
    }
}
