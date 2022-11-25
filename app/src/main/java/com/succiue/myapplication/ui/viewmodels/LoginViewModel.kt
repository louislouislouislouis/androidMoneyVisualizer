package com.succiue.myapplication.ui.viewmodels

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import com.succiue.myapplication.data.model.KichtaUserModel
import com.succiue.myapplication.utils.Constant
import com.succiue.myapplication.utils.multipleNonNull
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch


data class LoginUiState(
    var loading: Boolean = false,
    var loginEnable: Boolean = true
)

@AssistedFactory
interface LoginViewModelFactory {
    fun create(loginActivity: Activity): LoginViewModel
}

class LoginViewModel @AssistedInject constructor(
    @Assisted var loginActivity: Activity,
) : ViewModel() {

    //New values
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    //Firebase Final Auth
    private lateinit var auth: FirebaseAuth

    // Old necessities
    private lateinit var mGoogleSignInClient: GoogleSignInClient


    var uiState by mutableStateOf(LoginUiState())
        private set

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
                    .setServerClientId(Constant.WEB_CLIENT_ID)
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(Constant.WEB_CLIENT_ID)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(loginActivity, gso)
    }

    /**
     * Function Called on UserIntent
     */
    fun login() {
        // Remove Tap Methode Because cannot test it on Emulator, has to be done with real device
        //loginNewMethod()
        loginOldMethod()
    }

    /**
     * Function Called on UserIntent
     */
    fun logout() {

        // Disconnect and remove any trace of user
        try {
            Firebase.auth.signOut()
            mGoogleSignInClient.signOut()
            uiState.loginEnable = true
        } catch (e: Exception) {
            e.localizedMessage?.let {
                Log.d("LoginViewModel", it)
            } ?: run {
                Log.d("LoginViewModel", "Undefined Error")
            }
        }

        // Launch a new activity and finish loginActivity
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


    /**
     * Function useful to transform FirebaseUser to KichtaUser
     */
    private suspend fun generateKichtaUser(user: FirebaseUser): KichtaUserModel? {
        // Generate all the info if FirebaseUser is found
        val def = CompletableDeferred<KichtaUserModel?>()
        user.getIdToken(true).addOnSuccessListener {
            it.token?.let { token ->
                multipleNonNull(
                    user.uid,
                    user.displayName,
                    user.email,
                    token
                ) { id, displayN, mail, idTkn ->
                    def.complete(
                        KichtaUserModel(
                            idKichta = id,
                            idToken = idTkn,
                            email = mail,
                            displayName = displayN
                        )
                    )
                }
            }
        }.addOnFailureListener {
            // If Google server does not want to respond
            it.localizedMessage?.let { er ->
                Log.d("LoginViewModel", er)
            } ?: run {
                Log.d("LoginViewModel", "Undefined Error")
            }
            def.complete(null)
        }
        return def.await()
    }

    /**
     * Function to auto-connect
     */
    fun autoLogin() {
        // Get an instance of Firebase User
        val user = Firebase.auth.currentUser
        user?.let { myLastAccount ->
            // Get token from user
            uiState.loading = true
            myLastAccount.getIdToken(true).addOnSuccessListener { result ->
                val idToken = result.token
                Log.d("LoginViewModel", "GetTokenResult result = $idToken")
                uiState.loginEnable = false
                uiState.loading = false
            }.addOnFailureListener { error ->
                Log.d("LoginViewModel", "An error occurred= ${error.localizedMessage}")
            }
        } ?: run {
            Log.d("LoginViewModel", "No previous user found")
            uiState.loginEnable = true
            uiState.loading = false
        }
    }

    /**
     * CallBack of Google Activity
     */
    fun handleActivityResult(requestCode: Int, data: Intent?) {
        when (requestCode) {
            Constant.CODE_GOOGLE_RC_SIGN_IN -> {
                try {
                    val task: Task<GoogleSignInAccount> =
                        GoogleSignIn.getSignedInAccountFromIntent(data)
                    handleSignInResult(task)

                } catch (e: ApiException) {
                    e.localizedMessage?.let {
                        Log.d("LoginViewModel", it)
                    } ?: run {
                        Log.d("LoginViewModel", "Undefined Error")
                    }
                }
            }
            // Never Tested --> Outdated
            Constant.CODE_GOOGLE_REQ_ONE_TAP -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    when {
                        idToken != null -> {
                            Log.d("LoginViewModel", "Got ID token.")
                        }
                        else -> {
                            Log.d("LoginViewModel", "No ID token!")
                        }
                    }
                } catch (e: ApiException) {
                    e.localizedMessage?.let {
                        Log.d("LoginViewModel", it)
                    } ?: run {
                        Log.d("LoginViewModel", "Undefined Error")
                    }
                }
            }
        }
    }

    /**
     * Handle Old signing Method
     */
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            val firebaseCredential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener(loginActivity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("LoginViewModel", "signInWithCredential:success")
                        uiState = uiState.copy(loginEnable = false)
                        Firebase.auth.currentUser?.let {
                            // If b is not null
                            viewModelScope.launch {

                                val user = generateKichtaUser(it)
                                // Choose which screen to launch
                                val toLaunch = MainActivity::class.java

                                // Create intent for Activity
                                val intent = Intent(loginActivity, toLaunch)

                                // Add user as a parameter (only used in MainScreen)
                                intent.putExtra("user", user)

                                //Start new activity and quit this one
                                loginActivity.startActivity(intent)
                                loginActivity.finish()

                            }
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("LoginViewModel", "signInWithCredential:failure", task.exception)
                    }
                }
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("LoginViewModel", "signInResult:failed code=" + e.statusCode)

        }
    }

    private fun loginOldMethod() {
        loginActivity.startActivityForResult(
            mGoogleSignInClient.signInIntent,
            Constant.CODE_GOOGLE_RC_SIGN_IN
        )
    }


    /**
     * First Implementation of OneTap
     * As a cool Google function, never work on emulator
     * https://medium.com/firebase-developers/how-to-authenticate-to-firebase-using-google-one-tap-in-jetpack-compose-60b30e621d0d
     */
    private fun loginNewMethod() {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(loginActivity) { result ->
                try {
                    loginActivity.startIntentSenderForResult(
                        result.pendingIntent.intentSender, Constant.CODE_GOOGLE_REQ_ONE_TAP,
                        null, 0, 0, 0, null
                    )
                } catch (e: IntentSender.SendIntentException) {
                    Log.e("LoginViewModel", "Couldn't start One Tap UI: ${e.localizedMessage}")
                    loginOldMethod()
                }
            }
            .addOnFailureListener(loginActivity) { e ->
                // No saved credentials found. Launch the One Tap sign-up flow, or
                // do nothing and continue presenting the signed-out UI.
                e.localizedMessage?.let {
                    Log.d("LoginViewModel", it)
                } ?: run {
                    Log.d("LoginViewModel", "Undefined Error")
                }
                loginOldMethod()
            }
    }


}


