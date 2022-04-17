package com.findmyrecycling

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.findmyrecycling.dto.Facility
import com.findmyrecycling.ui.theme.FindMyRecyclingTheme
import com.findmyrecycling.ui.theme.RecyclingBlue
import com.findmyrecycling.ui.theme.RecyclingGray
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileScreen : ComponentActivity() {
    private val viewModel: MainViewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FindMyRecyclingTheme {
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
        val context = LocalContext.current

        Column {
            OutlinedTextField(
                value = inFacilityName,
                onValueChange = { inFacilityName = it },
                label = { Text(stringResource(R.string.facilityName), fontSize = 17.sp, fontWeight = FontWeight.W800) },
                modifier = Modifier.fillMaxWidth()
                    .background(color = RecyclingGray)
            )
            OutlinedTextField(
                value = inFacilityLocation,
                onValueChange = { inFacilityLocation = it },
                label = { Text(stringResource(R.string.facilityLocation), fontSize = 17.sp, fontWeight = FontWeight.W800) },
                modifier = Modifier.fillMaxWidth()
                    .background(color = RecyclingGray)
            )
            OutlinedTextField(
                value = inFacilityDetails,
                onValueChange = { inFacilityDetails = it },
                label = { Text(stringResource(R.string.facilityDetails), fontSize = 17.sp, fontWeight = FontWeight.W800) },
                modifier = Modifier.fillMaxWidth()
                    .background(color = RecyclingGray)
            )
            OutlinedTextField(
                value = inRecyclableProduct,
                onValueChange = { inRecyclableProduct = it },
                label = { Text(stringResource(R.string.recyclableProduct), fontSize = 17.sp, fontWeight = FontWeight.W800) },
                modifier = Modifier.fillMaxWidth()
                    .background(color = RecyclingGray)
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
                        viewModel.saveFacility()
                        Toast.makeText(
                            context,
                            "$inFacilityName $inFacilityLocation $inFacilityDetails",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                ) {
                    Text(text = "Save", color = RecyclingBlue, fontSize = 17.sp)
                }
                Button(
                    onClick = {

                    }
                )
                {
                    Text(text = "Camera", color = RecyclingBlue, fontSize = 17.sp)
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