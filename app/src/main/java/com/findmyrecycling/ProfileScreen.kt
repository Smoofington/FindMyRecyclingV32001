package com.findmyrecycling

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
fun ProfileOptions(product: String) {
    var profile by remember{ mutableStateOf("") }
    var mySavedLocations by remember{ mutableStateOf("") }
    var mySavedSearches by remember{ mutableStateOf("") }

    Column {
        TextField(
            value = profile,
            onValueChange = {profile = it},
            label = { Text(stringResource(R.string.profile)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp, top = 2.dp, bottom = 2.dp)
                .border(width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(4.dp)),

        )

        TextField(
            value = mySavedLocations,
            onValueChange = { mySavedLocations = it },
            label = { Text(stringResource(R.string.mySavedLocations)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp, top = 2.dp, bottom = 2.dp)
                .border(width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(4.dp)),
        )

        TextField(
            value = mySavedSearches,
            onValueChange = { mySavedSearches = it },
            label = { Text(stringResource(R.string.mySavedSearches)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp, top = 2.dp, bottom = 2.dp)
                .border(width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(4.dp)),
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