package com.android.androidinternals

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.androidinternals.ui.theme.MyAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(paddingValues = innerPadding)
                }
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MainScreen(viewModel: MainViewModel = viewModel(), paddingValues: PaddingValues) {

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bmp ->
        bmp?.let {
            val saved = viewModel.saveToInternalStorage(context, bmp)
            if (saved) {
                Toast.makeText(context, "Image saved to internal storage", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Failed to save image to internal storage", Toast.LENGTH_LONG).show()
            }
        }
    }

    val bitmap = viewModel.bitmap.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(paddingValues),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        bitmap.value?.let {
            Image(
                modifier = Modifier.width(250.dp).height(250.dp),
                bitmap = it.asImageBitmap(),
                contentDescription = null)
        }
        Button(
            modifier = Modifier.padding(top = 15.dp),
            onClick = {
                launcher.launch(null)
            }) {
            Text(text = "Capture Photo")
        }
        Button(
            modifier = Modifier.padding(top = 15.dp),
            onClick = {
                coroutineScope.launch {
                    val success = viewModel.readImageFromInternalStorage()
                    if (success) {
                        Toast.makeText(context, "Read latest photo is successful", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Read latest photo is not successful", Toast.LENGTH_LONG).show()
                    }
                }
            }) {
            Text(text = "Read Saved Photo")
        }

    }
}

