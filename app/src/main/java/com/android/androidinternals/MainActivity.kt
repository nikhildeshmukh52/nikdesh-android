package com.android.androidinternals

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.androidinternals.ui.theme.MyAppTheme

//Clients of AIDL in real Scenario belongs to different process.For Simplicity, in this eg, it is part of same process
class MainActivity : ComponentActivity() {
    private var remoteService: IProduct? = null
    private val viewModel: ProductViewModel by viewModels()

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            remoteService = IProduct.Stub.asInterface(binder)
            viewModel.connectService()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            remoteService = null
            viewModel.disconnectService()
        }

    }

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

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, ProductService::class.java)
        bindService(intent,serviceConnection, BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        unbindService(serviceConnection)
    }

    @Composable
    fun MainScreen(viewModel: ProductViewModel = viewModel(), paddingValues: PaddingValues) {

        val serviceConnected = viewModel.serviceConnected.collectAsStateWithLifecycle()
        val productInfo = viewModel.productInfo.collectAsStateWithLifecycle()

        Column(
            modifier = Modifier.padding(paddingValues).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (serviceConnected.value) {
                Text(text = "PackageName: ${productInfo.value.packageName}")
                Text(text = "PID: ${productInfo.value.pid}")
                Text(text = "User: ${productInfo.value.user}")
            }

            Button(onClick = {
                if(viewModel.serviceConnected.value) {
                    viewModel.updateProductInfo(remoteService!!)
                }
            }) {
                Text("Get Product Info")
            }
        }
    }


}

