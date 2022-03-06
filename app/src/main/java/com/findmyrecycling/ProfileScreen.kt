package com.findmyrecycling

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
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
fun ProfileOptions(name: String) {
    var profile by remember{ mutableStateOf("") }
    var mySavedLocations by remember{ mutableStateOf("") }
    var mySavedSearches by remember{ mutableStateOf("") }

    Column {
        OutlinedTextField(
            value = profile,
            onValueChange = {profile = it},
            label = { Text(stringResource(R.string.profile)) },
            modifier = Modifier.fillMaxWidth()
        )


        OutlinedTextField(
            value = mySavedLocations,
            onValueChange = { mySavedLocations = it },
            label = { Text(stringResource(R.string.mySavedLocations)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = mySavedSearches,
            onValueChange = { mySavedSearches = it },
            label = { Text(stringResource(R.string.mySavedSearches)) },
            modifier = Modifier.fillMaxWidth()
        )

    }
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    FindMyRecyclingTheme {
        ProfileOptions("Android")
    }
}