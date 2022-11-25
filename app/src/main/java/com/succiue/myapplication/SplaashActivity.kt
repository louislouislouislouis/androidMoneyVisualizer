package com.succiue.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.succiue.myapplication.ui.screens.SplashScreen
import com.succiue.myapplication.ui.theme.MyApplicationTheme
import com.succiue.myapplication.ui.viewmodels.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplaashActivity : ComponentActivity() {

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
            val kichtaUser = viewModel.googleAutoLogin()

            // Choose which screen to launch
            val toLaunch =
                if (kichtaUser == null) LoginActivity::class.java else MainActivity::class.java

            Log.d("Splaash Activity", "To Launch = $toLaunch")
            // Create intent for Activity
            val intent = Intent(this@SplaashActivity, toLaunch)

            // Add user as a parameter (only used in MainScreen)
            intent.putExtra("user", kichtaUser)

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
                ) {
                    SplashScreen()
                }
            }
        }
    }
}
