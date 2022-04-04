package com.findmyrecycling

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.findmyrecycling.dto.Product
import com.findmyrecycling.ui.theme.FindMyRecyclingTheme
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

var selectedProduct: Product? = null

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
           // MainViewModel.FetchProducts()
            val products = ArrayList<Product>()
            products.add(Product(product = "Tin Can", id = 0))
            products.add(Product(product = "Car Door", id = 1))
            products.add(Product(product = "Glass", id = 2))
            FindMyRecyclingTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background,
                modifier = Modifier.fillMaxWidth()) {
                    RecycleSearch(getString(R.string.Android), products)
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
            Button (
                onClick = {
                   // Toast.makeText(context, "$name")
                }
                    )
            {
                Text(text = stringResource(R.string.Save))
            }


        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        FindMyRecyclingTheme {
            RecycleSearch(getString(R.string.Android))
        }

        Button(
            onClick = {
                signIn()
            }
        ) {
            Text(text = stringResource(R.string.Logon))
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
}
