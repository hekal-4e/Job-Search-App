package com.depi.jobsearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.depi.jobsearch.ui.screens.ProfileScreen
import com.depi.jobsearch.ui.screens.previewUser
import com.depi.jobsearch.ui.theme.JobSearchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JobSearchTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ProfileScreen(
                        onEditClick = {  },
                        onSettingsClick = {  },
                        modifier = Modifier.padding(innerPadding),
                        userProfile = previewUser()
                    )
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    JobSearchTheme {
        ProfileScreen(
            onEditClick = {},
            onSettingsClick = {},
            modifier = Modifier,
            userProfile = previewUser()
        )
    }
}
