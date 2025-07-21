package com.android.androidinternals

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.android.androidinternals.ui.theme.MyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Button(
                        onClick = {
                            val intent = Intent("com.android.androidinternals.ACTION_GREETING").apply {
                                `package` = "com.android.androidinternals"
                            }
                            sendBroadcast(intent)
                        },
                        modifier = Modifier.padding(innerPadding).fillMaxSize().wrapContentSize()
                    ) {
                        Text(text = "Send Greeting Broadcast")
                    }
                }
            }
        }
    }
}
