package com.findmyrecycling


import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.findmyrecycling.ui.theme.BarGray
import com.findmyrecycling.ui.theme.Beigeish
import com.findmyrecycling.ui.theme.FindMyRecyclingTheme
import com.findmyrecycling.ui.theme.RecyclingGreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FindMyRecyclingTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.surface,
                modifier = Modifier.fillMaxWidth()) {
                    //TopText("WWW")
                    RecycleSearch("Android")

                }
            }
        }
    }
}
@Composable
fun TopText(name: String) {

    Text(text = "Find My Recycling", color = Beigeish)
    //Modifier.background(color = RecyclingGreen)
}
@Composable
fun RecycleSearch(name: String) {
    var recyclable by remember{ mutableStateOf("")}
    var location by remember{ mutableStateOf("")}
    val context = LocalContext.current

    Column {

        Row {

            TopText("WWW")
            Button (
                onClick = {
                    Toast.makeText(context, "SS", Toast.LENGTH_LONG).show()
                },
                content = {Text(text = "Profile")},
                modifier = Modifier.padding(70.dp, 0.dp)
            )
        }

        OutlinedTextField(
            value = recyclable,
            onValueChange = {recyclable = it},
            label = { Text(stringResource(R.string.recyclable)) },
            modifier = Modifier
                .fillMaxWidth()
                .background(BarGray)


        )


        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text(stringResource(R.string.location)) },
            modifier = Modifier
                .fillMaxWidth()
                .background(color = BarGray)
        )

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FindMyRecyclingTheme {
        TopText("WWW")
        RecycleSearch("Android")
    }
}