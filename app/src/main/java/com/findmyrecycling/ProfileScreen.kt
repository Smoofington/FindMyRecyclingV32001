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
import com.findmyrecycling.dto.Facility
import com.findmyrecycling.ui.theme.FindMyRecyclingTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileScreen : ComponentActivity() {
    private val viewModel: MainViewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FindMyRecyclingTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colors.background,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ProfileOptions("Android")
                }
            }
        }
    }


    @Composable
    fun MainMenu() {
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            Icon(imageVector = Icons.Filled.Menu, contentDescription = "")
        }
    }

    @Composable
    fun ProfileOptions(name: String) {
        var inFacilityName by remember { mutableStateOf("") }
        var inFacilityLocation by remember { mutableStateOf("") }
        var inFacilityDetails by remember { mutableStateOf("") }
        var inRecyclableProduct by remember { mutableStateOf("") }

        Column {
            OutlinedTextField(
                value = inFacilityName,
                onValueChange = { inFacilityName = it },
                label = { Text(stringResource(R.string.facilityName)) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = inFacilityLocation,
                onValueChange = { inFacilityLocation = it },
                label = { Text(stringResource(R.string.facilityLocation)) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = inFacilityDetails,
                onValueChange = { inFacilityDetails = it },
                label = { Text(stringResource(R.string.facilityDetails)) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = inRecyclableProduct,
                onValueChange = { inRecyclableProduct = it },
                label = { Text(stringResource(R.string.recyclableProduct)) },
                modifier = Modifier.fillMaxWidth()
            )
            Row {
                Button(
                    onClick = {
                        var facility = Facility().apply {
                            this.facilityName = inFacilityName
                            this.location = inFacilityLocation
                            this.description = inFacilityDetails
                            this.recyclableProducts = inRecyclableProduct
                        }
                        viewModel.save(facility)
                    }
                ) {
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
}