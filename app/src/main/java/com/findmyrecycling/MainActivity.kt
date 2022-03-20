package com.findmyrecycling


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.findmyrecycling.ui.theme.FindMyRecyclingTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel : MainViewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            viewModel.fetchProducts()
            val products by viewModel.products.observeAsState(initial = emptyList())
            FindMyRecyclingTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background,
                modifier = Modifier.fillMaxWidth()) {
                    RecycleSearch("Android")
                }
            }
        }
    }
}

@Composable
fun RecycleSearch(name: String) {
    var recyclable by remember{ mutableStateOf("")}
    var location by remember{ mutableStateOf("")}

    Column {
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

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FindMyRecyclingTheme {
        RecycleSearch("Android")
    }
}