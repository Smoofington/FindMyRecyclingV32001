package com.findmyrecycling


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.findmyrecycling.ui.theme.FindMyRecyclingTheme
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
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
fun RecycleSearch(product: String) {
    var recyclable by remember{ mutableStateOf("") }
    var location by remember{ mutableStateOf("") }

    Column {
        OutlinedTextField(
            value = recyclable,
            onValueChange = {recyclable = it},
            label = { Text(stringResource(R.string.recyclable)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp, top = 2.dp, bottom = 2.dp)
                .border(width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(4.dp)),
        )

        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text(stringResource(R.string.location)) },
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
fun DefaultPreview() {
    FindMyRecyclingTheme {
        RecycleSearch("Android")
    }

    Button(
        onClick = {
            signIn()
        }
    ) {
        Text(text = "Logon")
    }
}

fun signIn() {
    val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())
    val signinIntent = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(providers)
        .build()

    signInLauncher.launch(signinIntent)
}

private val signInLauncher = registerForActivityResult (
    FirebaseAuthUIActivityResultContract()
) {
        res -> this.signInResult(res)
}


private fun signInResult(result: FirebaseAuthUIAuthenticationResult) {
    val response = result.idpResponse
    if (result.resultCode == ComponentActivity.RESULT_OK) {
        var user = FirebaseAuth.getInstance().currentUser
    }