package com.findmyrecycling


import android.graphics.drawable.Drawable
import android.media.Image
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import com.findmyrecycling.dto.Product
import com.findmyrecycling.ui.theme.FindMyRecyclingTheme
import com.google.common.collect.Collections2.filter
import com.google.common.collect.Iterables.filter
import com.google.common.collect.Iterators.filter
import com.google.common.collect.Multisets.filter
import com.google.common.collect.Sets.filter
import java.util.Locale.filter

var selectedProduct: Product? = null

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
           // MainViewModel.FetchProducts()
            val products = ArrayList<Product>()
            products.add(Product(name = "Tin Can", product = "pTin Can", id = 0))
            products.add(Product(name = "Car Door", product = "pCar Door", id = 1))
            products.add(Product(name = "Glass", product = "pGlass", id = 2))
            FindMyRecyclingTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background,
                modifier = Modifier.fillMaxWidth()) {
                    RecycleSearch("Android", products)
                }
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
            modifier = Modifier.fillMaxWidth()
        )


        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text(stringResource(R.string.location)) },
            modifier = Modifier.fillMaxWidth()
        )
        Button (
            onClick = {
               // Toast.makeText(context, "$name")
            }
                )
        {
            Text(text = "Save")
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