package com.findmyrecycling


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.findmyrecycling.ui.theme.FindMyRecyclingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FindMyRecyclingTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun RecycleSearch(name: String) {
    var recyclable by remember{ mutableStateOf("")}
    var location by remember{ mutableStateOf("")}

    Column {
        OutlinedTextField(
            value = recyclable,
            onValueChange = {recyclable = it},
            label = { Text(stringResource(R.string.recyclable)) }
        )


        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text(stringResource(R.string.location)) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FindMyRecyclingTheme {
        Greeting("Android")
        RecycleSearch("AAA")
    }
}