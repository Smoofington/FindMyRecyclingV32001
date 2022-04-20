package com.findmyrecycling


import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight.Companion.W800
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import coil.compose.AsyncImage
import com.findmyrecycling.dto.Product
import com.findmyrecycling.ui.theme.FindMyRecyclingTheme
import com.findmyrecycling.ui.theme.RecyclingBlue
import com.findmyrecycling.ui.theme.RecyclingGray
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : ComponentActivity() {

    var selectedProduct: Product? = null
    private var strSelectedData: String =""
    private val viewModel: MainViewModel by viewModel<MainViewModel>()
    private var strUri by mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            viewModel.fetchProducts()
            val products by viewModel.products.observeAsState(initial = emptyList())
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
    fun RecycleSearch(name: String, products : List<Product> = ArrayList<Product>(), selectedProduct: Product = Product()) {
        var location by remember { mutableStateOf("") }
        Row {
            Arrangement.Center
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
                        addFacility()
                    }
                )
                {
                    Text(text = "Add Facility", color = RecyclingBlue, fontSize = 17.sp)
                }
            }
            AsyncImage(model = strUri, contentDescription = "Facility image")
        }
    }

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

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        FindMyRecyclingTheme {
            RecycleSearch("Android")
        }
    }

    private fun addFacility() {
        val intent = Intent(this, NewFacilityActivity::class.java)
        startActivity(intent)
    }
}
