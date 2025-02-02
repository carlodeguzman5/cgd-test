package com.example.splashapplication

import android.os.Bundle
import android.window.SplashScreen
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.splashapplication.ui.theme.SplashApplicationTheme
import kotlinx.coroutines.delay
import androidx.navigation.compose.composable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            SplashApplicationTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
//            }
//        }
        setContent {
            SplashApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "splash",
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        composable(route = "splash") {
                            SplashScreen(navController)
                        }
                        composable(route = "main") {
                            MainContent()
//                            MyLayout()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SplashScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        LaunchedEffect(key1 = true) {
            delay(2000) // Delay for 2 seconds
            navController.navigate("main") // Navigate to the "main" route
        }
    }
}

@Composable
fun MainNav() {
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(Color.Gray)
//            .weight(1f)
//    )
}

@Composable
fun MyLayout() {
    Column(modifier = Modifier.fillMaxSize()) { // Make Column fill the screen if needed
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray)
                .weight(1f) // <--- weight is correctly applied here in the Column
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background), // Replace with your image resource
                contentDescription = "Image",
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Some text",
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Divider(
            color = Color.Gray,
            thickness = 1.dp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Some text at the bottom",
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.CenterHorizontally) // Optional: Center text horizontally
        )
    }
}

//
@Composable
fun MainContent() {
    Column(
        modifier = Modifier.fillMaxSize() // Fill the available height and width
    ) {
        // First part of the column with a Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // This makes the Box take up the remaining space
                .background(Color.Gray)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "Image",
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        // First Text below the Box
        Text(
            text = "Some text",
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Divider(
            color = Color.Gray,
            thickness = 1.dp
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Second Text
        Text(
            text = "Some text",
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}