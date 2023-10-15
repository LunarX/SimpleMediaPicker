package com.lunarx.libraries.simplemediapicker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lunarx.libraries.simplemediapicker.ui.theme.SimpleMediaPickerTheme

class MainActivity : ComponentActivity() {

    val mediaPicker = MediaPicker(this) {
        Log.e("gibran", "addImage: it = ${it}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleMediaPickerTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting("Android")

                    TextButton(onClick = {
                        mediaPicker.chooseMedia(MediaPicker.FileType.IMAGE)
                    },
                        modifier = Modifier.wrapContentSize()
                    ) {
                        Text(text = "Salut")
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
    SimpleMediaPickerTheme {
        Greeting("Android")
    }
}