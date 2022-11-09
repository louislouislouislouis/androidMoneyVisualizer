package com.succiue.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.succiue.myapplication.data.model.User
import com.succiue.myapplication.ui.screens.LoginHome
import com.succiue.myapplication.ui.viewmodels.LoginViewModel
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {
    private var loginViewModel = LoginViewModel(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background,
            ) {
                LoginHome(loginViewModel)
            }

        }
        loginViewModel.initViewModel(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        loginViewModel.handleActivityResult(requestCode, resultCode, data)

    }

    fun resetApp(user: User) {
        lifecycleScope.launch {

            //Suspend function, lock the execution of the next code

            // Choose which screen to launch
            val toLaunch = if (user == null) LoginActivity::class.java else MainActivity::class.java

            // Create intent for Activity
            val intent = Intent(this@LoginActivity, toLaunch)

            // Add user as a parameter (only used in MainScreen)
            intent.putExtra("user", user)

            //Start new activity and quit this one
            startActivity(intent)
            finish()

        }
    }


    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        loginViewModel.isThereAnyPerson()
    }
}