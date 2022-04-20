package com.findmyrecycling

import android.Manifest
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.W800
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModel
import coil.compose.AsyncImage
import com.findmyrecycling.dto.Facility
import com.findmyrecycling.dto.Photo
import com.findmyrecycling.dto.Product
import com.findmyrecycling.dto.User
import com.findmyrecycling.ui.theme.*
import com.google.common.collect.Collections2.filter
import com.google.common.collect.Iterables.filter
import com.google.common.collect.Iterators.filter
import com.google.common.collect.Multisets.filter
import com.google.common.collect.Sets.filter
import java.util.Locale.filter
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


class MainActivity : ComponentActivity() {

    var selectedProduct: Product? = null
    var selectedFacility: Facility? = null
    private var strSelectedData: String =""
    private var uri: Uri? = null
    private lateinit var currentImagePath: String
    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private val viewModel: MainViewModel by viewModel<MainViewModel>()
    private var inProductName: String = ""
    private var strUri by mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            viewModel.fetchProducts()
            val products by viewModel.products.observeAsState(initial = emptyList())
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
                    RecycleSearch("Android", products)
                }
            }
        }
    }

    @Composable
    fun ProductSpinner(products: List<Product>) {
        var productText by remember { mutableStateOf("Product List") }
        var expanded by remember { mutableStateOf(false) }
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {

            Row(Modifier
                .padding(24.dp)
                .clickable {
                    expanded = !expanded
                }
                .padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = productText, fontSize = 18.sp, modifier = Modifier.padding(end = 8.dp))
                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "")
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    products.forEach { product ->
                        DropdownMenuItem(onClick = {
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
    fun RecycleSearch(name: String, products : List<Product> = ArrayList<Product>(), selectedProduct: Product = Product()) {
        var recyclable by remember (selectedProduct.productId){ mutableStateOf(selectedProduct.product) }
        var location by remember { mutableStateOf("") }
        val context = LocalContext.current
        Row {
            Arrangement.Center
            //ProfileMenu()
        }

        Column {
            TextFieldWithDropdownUsage(
                dataIn = products,
                "Recyclable",
                3,
                Product()
            )


            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text(stringResource(R.string.location), fontSize = 17.sp, fontWeight = W800) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 4.dp, top = 2.dp, bottom = 2.dp)
                    .background(color = RecyclingGray)
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(4.dp)
                    ),
            )
            Row(modifier = Modifier
                .padding(start = 4.dp)

            ) {
                Button(
                    onClick = {
                        viewModel.saveFacility()
                    }
                )
                {
                    Text(text = "Save", color = RecyclingBlue, fontSize = 17.sp)
                }
                Button(
                    onClick = {
                        takePhoto()
                    }
                )
                {
                    Text(text = "Photo", color = RecyclingBlue, fontSize = 17.sp)
                }
                Button(
                    onClick = {
                        addFacility()
                    }
                )
                {
                    Text(text = "Add Facility", color = RecyclingBlue, fontSize = 17.sp)
                }
                Button(
                    onClick = {
                        signIn()
                    }
                )
                {
                    Text(text = "Log On", color = RecyclingBlue, fontSize = 17.sp)
                }
            }
            AsyncImage(model = strUri, contentDescription = "Facility image")
        }
    }

    //list of stuff added
    @Composable
    fun TextFieldWithDropdownUsage(dataIn: List<Product>, label : String = "", take :Int = 3, selectedProduct : Product = Product()) {

        val dropDownOptions = remember { mutableStateOf(listOf<Product>()) }
        val textFieldValue = remember(selectedProduct.productId) { mutableStateOf(TextFieldValue(selectedProduct.product)) }
        val dropDownExpanded = remember { mutableStateOf(false) }

        fun onDropdownDismissRequest() {
            dropDownExpanded.value = false
        }

        fun onValueChanged(value: TextFieldValue) {
            strSelectedData = value.text
            dropDownExpanded.value = true
            textFieldValue.value = value
            dropDownOptions.value = dataIn.filter {
                it.toString().startsWith(value.text) && it.toString() != value.text
            }.take(take)
        }

        TextFieldWithDropdown(
            modifier = Modifier.fillMaxWidth()

                .padding(start = 4.dp, end = 4.dp, top = 2.dp, bottom = 2.dp)
                .background(color = RecyclingGray)
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(4.dp)
                ),
            value = textFieldValue.value,
            setValue = ::onValueChanged,
            onDismissRequest = ::onDropdownDismissRequest,
            dropDownExpanded = dropDownExpanded.value,
            list = dropDownOptions.value,
            label = label
        )
    }
    @Composable
    fun TextFieldWithDropdown(
        modifier: Modifier = Modifier,
        value: TextFieldValue,
        //fontWeight: FontWeight,
        setValue: (TextFieldValue) -> Unit,
        onDismissRequest: () -> Unit,
        dropDownExpanded: Boolean,
        list: List<Product>,
        label: String = ""
    ) {
        Box(modifier) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        if (!focusState.isFocused)
                            onDismissRequest()
                    },
                value = value,
                //fontWeight =W800,
                onValueChange = setValue,
                label = { Text((label), fontSize = 17.sp, fontWeight = W800) },
                colors = TextFieldDefaults.outlinedTextFieldColors()

            )
            DropdownMenu(
                expanded = dropDownExpanded,
                properties = PopupProperties(
                    focusable = false,
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                ),
                onDismissRequest = onDismissRequest
            ) {
                list.forEach { text ->
                    DropdownMenuItem(onClick = {
                        setValue(
                            TextFieldValue(
                                text.toString(),
                                TextRange(text.toString().length)
                            )
                        )
                        selectedProduct = text
                    }) {
                        Text(text = text.toString())
                    }
                }
            }
        }
    }




    //end of stuff added
    private fun takePhoto() {
        if (hasCameraPermission() == PERMISSION_GRANTED && hasExternalStoragePermission() == PERMISSION_GRANTED) {
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
            Log.e(TAG, "Error: ${e.message}")
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

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        FindMyRecyclingTheme {
            RecycleSearch("Android")
        }
    }

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

    private fun addFacility() {
        val intent = Intent(this, ProfileScreen::class.java)
        startActivity(intent)
    }
}
