package com.succiue.myapplication.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.succiue.myapplication.data.model.KichtaUserModel
import com.succiue.myapplication.utils.multipleNonNull
import kotlinx.coroutines.CompletableDeferred

class SplashViewModel : ViewModel() {

    /**
     * FirebaseUser, as we use Firebase for Authentication
     */
    private val user: FirebaseUser? = Firebase.auth.currentUser

    suspend fun googleAutoLogin(): KichtaUserModel? {
        //if there is no FirebaseUser saved in mobile, don't need to dp that
        if (user != null) {
            // Generate all the info if FirebaseUser is found
            val def = CompletableDeferred<KichtaUserModel?>()
            user?.getIdToken(true)?.addOnSuccessListener { tokenResult ->
                tokenResult.token?.let { token ->
                    val displayName = user?.displayName
                    val idKichta = user?.uid
                    val email = user?.email

                    multipleNonNull(
                        idKichta,
                        displayName,
                        email,
                        token
                    ) { id, displayN, mail, idTkn ->
                        def.complete(
                            KichtaUserModel(
                                idKichta = id,
                                displayName = displayN,
                                email = mail,
                                idToken = idTkn
                            )
                        )
                    }
                }
            }?.addOnFailureListener {
                // If Google server does not want to respond
                Log.d("SplashViewModel", it.localizedMessage)
                def.complete(null)
            }
            return def.await()
        } else {
            return null
        }
    }
}