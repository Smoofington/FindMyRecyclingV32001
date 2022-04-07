package com.findmyrecycling

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.findmyrecycling.ui.theme.FindMyRecyclingTheme

class ProfileScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FindMyRecyclingTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background,
                    modifier = Modifier.fillMaxWidth()) {
                    ProfileOptions("Android")
                }
            }
        }
    }
}

@Composable
fun MainMenu(){
    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
        Icon(imageVector = Icons.Filled.Menu, contentDescription = "")

    }
}

@Composable
fun ProfileOptions(name: String) {
    var facilityName by remember{ mutableStateOf("")}
    var facilityLocation by remember{ mutableStateOf("")}
    var facilityDetails by remember{ mutableStateOf("")}
    var recyclableProduct by remember{ mutableStateOf("")}

    Column {
        OutlinedTextField(
            value = facilityName,
            onValueChange = {facilityName = it},
            label = { Text(stringResource(R.string.facilityName)) },
            modifier = Modifier.fillMaxWidth()
        )


        OutlinedTextField(
            value = facilityLocation,
            onValueChange = { facilityLocation = it },
            label = { Text(stringResource(R.string.facilityLocation)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = facilityDetails,
            onValueChange = { facilityDetails = it },
            label = { Text(stringResource(R.string.facilityDetails)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = recyclableProduct,
            onValueChange = { recyclableProduct = it },
            label = { Text(stringResource(R.string.recyclableProduct)) },
            modifier = Modifier.fillMaxWidth()
        )
        Row {
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
                    // Toast.makeText(context, "$name")
                }
            )
            {
                Text(text = "Photo")
            }
        }

    }
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    FindMyRecyclingTheme {
        ProfileOptions("Android")
    }
}