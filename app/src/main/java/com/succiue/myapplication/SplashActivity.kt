package com.succiue.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.succiue.myapplication.ui.theme.MyApplicationTheme
import com.succiue.myapplication.ui.viewmodels.SplashViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : ComponentActivity() {

    /**
     * Minimum Time in microseconds where the SplashScreen as to be shown
     */
    private var minimumTime = 3000

    /**
     * ViewModel for SplashScreen
     */
    private var viewModel: SplashViewModel = SplashViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Save launchedTime
        val startTime: Long = System.currentTimeMillis()

        //As to be done in a coroutine, as there is asynchronous call
        lifecycleScope.launch {

            //Suspend function, lock the execution of the next code
            val user = viewModel.isThereAnyPerson()

            // Choose which screen to launch
            val toLaunch = if (user == null) LoginActivity::class.java else MainActivity::class.java

            // Create intent for Activity
            val intent = Intent(this@SplashActivity, toLaunch)

            // Add user as a parameter (only used in MainScreen)
            intent.putExtra("user", user)

            //Add additional delay if too quick
            delay(minimumTime - (System.currentTimeMillis() - startTime))

            //Start new activity and quit this one
            startActivity(intent)
            finish()

        }

        // TODO: Change the look
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Our Splash Screen")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}