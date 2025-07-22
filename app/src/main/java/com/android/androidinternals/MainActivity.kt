package com.android.androidinternals

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.androidinternals.ui.theme.MyAppTheme

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

    @Composable
    fun MainScreen(viewModel: RandomNumberViewModel = viewModel(), paddingValues: PaddingValues) {

        val serviceConnected = viewModel.boundService.collectAsStateWithLifecycle()
        val randomNumber = viewModel.randomNumber.collectAsStateWithLifecycle()
        Log.i("MainScreen", "${randomNumber.value}")

        var randomService: RandomNumberService? by remember { mutableStateOf(null) }

        val serviceConnection = remember {
            object : ServiceConnection {
                override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
                    val binder = service as RandomNumberService.LocalBinder
                    randomService = binder.getService()
                    viewModel.serviceConnected()
                }

                override fun onServiceDisconnected(p0: ComponentName?) {
                    randomService = null
                    viewModel.serviceDisconnected()
                }
            }
        }

        val context = LocalContext.current

        DisposableEffect(key1 = Unit) {
            val intent = Intent(context, RandomNumberService::class.java)
            context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)

            onDispose {
                context.unbindService(serviceConnection)
            }
        }

        Column(modifier = Modifier.padding(paddingValues).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Random Number: ${randomNumber.value}")
            Button(
                onClick = {
                    if (serviceConnected.value) randomService?.let { viewModel.generateRandomNumber(it) }
                }
            ) {
                Text(text = "Generate Random Number", color = Color.Cyan)
            }
        }
    }
}

