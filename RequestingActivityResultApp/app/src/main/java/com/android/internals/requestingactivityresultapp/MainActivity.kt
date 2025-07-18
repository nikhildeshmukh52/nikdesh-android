package com.android.internals.requestingactivityresultapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.android.internals.requestingactivityresultapp.ui.theme.RequestingActivityResultAppTheme

class MainActivity : ComponentActivity() {

    private var greeting = "Launch Activity for Greeting"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RequestingActivityResultAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val launcher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartActivityForResult()
                    ) { activityResult ->
                        if(activityResult.resultCode == RESULT_OK) {
                            val greeting = activityResult.data?.getStringExtra("greeting").toString()
                            Toast.makeText(this, greeting, Toast.LENGTH_SHORT).show()
                        }
                    }
                    Button(
                        onClick = {
                            val intent = Intent("com.androidinternals.greeting.request")
                            launcher.launch(intent)
                        },
                        modifier = Modifier.fillMaxSize().padding(innerPadding).wrapContentSize()
                    ) {
                        Text(text = "Launch Activity for Greeting result")
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RequestingActivityResultAppTheme {
        Greeting("Android")
    }
}