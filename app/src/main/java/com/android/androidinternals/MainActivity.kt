package com.android.androidinternals

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.androidinternals.ui.theme.MyAppTheme

//Messenger Service is meant to be used for IPC. For Simplicity it is part of same process in this application
class MainActivity : ComponentActivity() {

    private var serverMessenger: Messenger? = null
    private var clientMessenger: Messenger? = null

    companion object {
        const val REQUEST_PACKAGE_INFO = 1
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

    @Composable
    fun MainScreen(viewModel: PackageInfoViewModel = viewModel(), paddingValues: PaddingValues) {

        val handler = remember {
            object : Handler(Looper.getMainLooper()) {
                override fun handleMessage(msg: Message) {
                    super.handleMessage(msg)
                    val bundle = msg.data
                    val packageData = PackageData(
                        bundle.getString(PackageInfoKey.PACKAGE_NAME, ""),
                        bundle.getInt(PackageInfoKey.PID, 0),
                        bundle.getString(PackageInfoKey.USER, "")
                    )
                    viewModel.updatePackageInfo(packageData)
                }

            }
        }

        val serviceConnection = remember {
            object : ServiceConnection {
                override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
                    serverMessenger = Messenger(service)
                    viewModel.serviceConnected()
                }

                override fun onServiceDisconnected(p0: ComponentName?) {
                    serverMessenger = null
                    viewModel.serviceDisconnected()
                }

            }
        }

        val context = LocalContext.current
        DisposableEffect(key1 = Unit) {
            val intent = Intent(context, PackageMessengerService::class.java)
            bindService(intent, serviceConnection, BIND_AUTO_CREATE)
            clientMessenger = Messenger(handler)

            onDispose {
                unbindService(serviceConnection)
            }

        }

        val serviceBounded = viewModel.boundService.collectAsStateWithLifecycle()
        val packageInfo = if(serviceBounded.value) viewModel.packageInfo.collectAsStateWithLifecycle().value else PackageData()

        Column(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            Text(text = "pkg: ${packageInfo.packageName}", color = Color.Black)
            Text(text = "pid: ${packageInfo.pid}", color = Color.Black)
            Text(text = "user: ${packageInfo.user}", color = Color.Black)

            Button(
                onClick = {
                    val msg: Message = Message.obtain(null, REQUEST_PACKAGE_INFO, 0, 0)
                    msg.replyTo = clientMessenger
                    if(serviceBounded.value) serverMessenger?.send(msg)
                }
            ) {
                Text("Request Package Info", color = Color.Blue)
            }

        }

    }
}
