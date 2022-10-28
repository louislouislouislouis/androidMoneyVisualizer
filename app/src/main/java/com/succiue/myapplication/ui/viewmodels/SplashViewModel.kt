package com.succiue.myapplication.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.succiue.myapplication.data.model.User
import com.succiue.myapplication.utils.multipleNonNull
import kotlinx.coroutines.CompletableDeferred

class SplashViewModel : ViewModel() {

    /**
     * FirebaseUser, as we use Firebase for Authentication
     */
    private val user: FirebaseUser? = Firebase.auth.currentUser

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


    suspend fun isThereAnyPerson(): User? {
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
}