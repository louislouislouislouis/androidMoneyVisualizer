package com.succiue.myapplication.ui.viewmodels

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.succiue.myapplication.LoginActivity
import com.succiue.myapplication.MainActivity
import com.succiue.myapplication.data.model.User
import com.succiue.myapplication.utils.multipleNonNull
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

class LoginViewModel(loginActivity: Activity) : ViewModel() {
    //Initialized once
    private var privateWebClientId =
        "473823468553-12tebj7jftchaasckr19oveqd8fsos1o.apps.googleusercontent.com"
    private var loginActivity = loginActivity

    //New values
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    //Custom value for signal
    companion object {
        var RC_SIGN_IN = 3
        var REQ_ONE_TAP = 2
    }
    // Can be any integer unique to the Activity

    //Firebase Final Auth
    private lateinit var auth: FirebaseAuth

    // Old necessities
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    val loginEnable = mutableStateOf(true)


    /**
     * idToken can be given by the FirebaseUser if google server has the amiability to respond
     */
    private var idToken: String? = null

    /**
     * uId can be given by the FirebaseUser if google server has the amiability to respond
     */
    private var uId: String? = null

    /**
     * displayName can be given by the FirebaseUser if google server has the amiability to respond
     */
    private var displayName: String? = null

    /**
     * email can be given by the FirebaseUser if google server has the amiability to respond
     */
    private var email: String? = null


    /**
     * Has to be called when the activity is started
     */
    fun initViewModel(activity: Activity) {
        auth = Firebase.auth
        oneTapClient = Identity.getSignInClient(activity)
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(privateWebClientId)
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(privateWebClientId)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(loginActivity, gso);
    }

    /**
     * Function Called on UserIntent
     */
    fun login() {
        logWithOneTapAndMightWellBeAnMFFailure()
    }

    fun logout() {
        try {
            Firebase.auth.signOut()
            mGoogleSignInClient.signOut();
            loginEnable.value = true;
        } catch (e: Exception) {
            Log.d("GOOGLEAUTH", e.localizedMessage)
        }


        // If b is not null
        viewModelScope.launch {


            // Choose which screen to launch
            val toLaunch = LoginActivity::class.java

            // Create intent for Activity
            val intent = Intent(loginActivity, toLaunch)


            //Start new activity and quit this one
            loginActivity.startActivity(intent)
            loginActivity.finish()

        }


    }


    suspend fun issThereAnyPerson(user: FirebaseUser): User? {
        //if there is no FirebaseUser saved in mobile, don't need to dp that
        if (user != null) {
            // Generate all the info if FirebaseUser is found
            val def = CompletableDeferred<User?>()
            user?.getIdToken(true)?.addOnSuccessListener {
                it.token?.let { token ->
                    idToken = token
                    displayName = user?.displayName
                    uId = user?.uid
                    email = user?.email
                    multipleNonNull(uId, displayName, email, idToken) { id, displayN, mail, idTkn ->
                        def.complete(User(id, displayN, mail, idTkn))
                    }
                }
            }?.addOnFailureListener {
                // If Google server does not want to respond
                Log.d("GOOGLEAUTH", it.localizedMessage)
                def.complete(null)
            }
            return def.await()
        } else {
            return null
        }
    }

    fun isThereAnyPerson() {
        val user = Firebase.auth.currentUser
        if (user != null) {
            var idToken = user.getIdToken(false)
            Log.d("GOOGLEAUTH", "I GOT IDTOKEN " + idToken.result.token)
            loginEnable.value = false
        }
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            RC_SIGN_IN -> {
                try {
                    val task: Task<GoogleSignInAccount> =
                        GoogleSignIn.getSignedInAccountFromIntent(data)
                    handleSignInResult(task)
                } catch (e: ApiException) {
                    Log.d("GOOGLEAUTH", e.localizedMessage)
                }
            }
            REQ_ONE_TAP -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    when {
                        idToken != null -> {
                            Log.d("GOOGLEAUTH", "Got ID token.")
                        }
                        else -> {
                            Log.d("GOOGLEAUTH", "No ID token!")
                        }
                    }
                } catch (e: ApiException) {
                    Log.d("GOOGLEAUTHv", e.localizedMessage)
                }
            }
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            val firebaseCredential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener(loginActivity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("GOOGLEAUTH", "signInWithCredential:success")
                        loginEnable.value = false

                        Firebase.auth.currentUser?.let {
                            // If b is not null
                            viewModelScope.launch {

                                val user = issThereAnyPerson(it)
                                // Choose which screen to launch
                                val toLaunch = MainActivity::class.java

                                // Create intent for Activity
                                val intent = Intent(loginActivity, toLaunch)

                                // Add user as a parameter (only used in MainScreen)
                                intent.putExtra("user", user)

                                //Add additional delay if too quick


                                //Start new activity and quit this one
                                loginActivity.startActivity(intent)
                                loginActivity.finish()

                            }

                        }


                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("GOOGLEAUTH", "signInWithCredential:failure", task.exception)
                    }
                }
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("GOOGLEAUTH", "signInResult:failed code=" + e.statusCode)

        }
    }

    private fun loginSigninOldMethodButAlwaysMFWork() {
        val signInIntent = mGoogleSignInClient.signInIntent
        loginActivity.startActivityForResult(signInIntent, RC_SIGN_IN)

    }


    /**
     * First Implementation of OneTap
     * As A cool Google function, never work on emulator
     * https://medium.com/firebase-developers/how-to-authenticate-to-firebase-using-google-one-tap-in-jetpack-compose-60b30e621d0d
     */
    private fun logWithOneTapAndMightWellBeAnMFFailure() {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(loginActivity) { result ->
                try {
                    loginActivity.startIntentSenderForResult(
                        result.pendingIntent.intentSender, REQ_ONE_TAP,
                        null, 0, 0, 0, null
                    )
                } catch (e: IntentSender.SendIntentException) {
                    Log.e("GOOGLEAUTH", "Couldn't start One Tap UI: ${e.localizedMessage}")
                    loginSigninOldMethodButAlwaysMFWork()
                }
            }
            .addOnFailureListener(loginActivity) { e ->
                // No saved credentials found. Launch the One Tap sign-up flow, or
                // do nothing and continue presenting the signed-out UI.
                Log.d("GOOGLEAUTH", e.localizedMessage)
                loginSigninOldMethodButAlwaysMFWork()
            }
    }


}