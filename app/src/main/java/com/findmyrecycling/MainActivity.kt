package com.findmyrecycling


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.findmyrecycling.ui.theme.FindMyRecyclingTheme
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.window.PopupProperties
import com.findmyrecycling.dto.Product
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: ProductViewModel by viewModel<ProductViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            viewModel.fetchProduct()
            val products by viewModel.products.observeAsState(initial = emptyList())
            FindMyRecyclingTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colors.background,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RecycleSearch(products)
                }
            }
        }
    }

    var inProductName = "No item selected"
    var selectedProduct = Product("", 0)

    @Composable
    fun RecycleSearch(products: List<Product> = ArrayList<Product>()) {
        var recyclable by remember { mutableStateOf("") }
        var location by remember { mutableStateOf("") }

        Column {
            TextFieldWithDropdownUsage(productsIn = products, stringResource(R.string.recyclable))


            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text(stringResource(R.string.location)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 4.dp, top = 2.dp, bottom = 2.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(4.dp)
                    ),
            )


        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        FindMyRecyclingTheme {
            RecycleSearch()
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

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.signInResult(res)
    }


    private fun signInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == ComponentActivity.RESULT_OK) {
            var user = FirebaseAuth.getInstance().currentUser
        }
    }

    @Composable
    fun TextFieldWithDropdownUsage(productsIn: List<Product>, label: String = "", take: Int= 3 ) {

        val dropDownOptions = remember { mutableStateOf(listOf<Product>()) }
        val textFieldValue = remember { mutableStateOf(TextFieldValue()) }
        val dropDownExpanded = remember { mutableStateOf(false) }

        fun onDropdownDismissRequest() {
            dropDownExpanded.value = false
        }

        fun onValueChanged(value: TextFieldValue) {
            inProductName = value.text
            dropDownExpanded.value = true
            textFieldValue.value = value
            dropDownOptions.value = productsIn.filter {
                it.toString().startsWith(value.text) && it.toString() != value.text
            }.take(3)


        }
        TextFieldWithDropdown(
            modifier = Modifier.fillMaxWidth(),
            value = textFieldValue.value,
            setValue = ::onValueChanged,
            onDismissRequest = ::onDropdownDismissRequest,
            dropDownExpanded = dropDownExpanded.value,
            list = dropDownOptions.value,
            label = "recyclable"
        )
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
    ) {
        Box(modifier) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        if (!focusState.isFocused)
                            onDismissRequest()
                    },
                value = value,
                onValueChange = setValue,
                label = { Text(label) },
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
                list.forEach { Product ->
                    DropdownMenuItem(onClick = {
                        setValue(
                            TextFieldValue(
                                Product.toString(),
                                TextRange(Product.toString().length)
                            )
                        )
                        selectedProduct = Product
                    }) {
                        Text(text = Product.toString())
                    }
                }
            }

        }

    }
}