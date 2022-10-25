package com.succiue.myapplication.data.model

import android.content.Context
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.MutableState
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.plaid.link.configuration.LinkTokenConfiguration
import com.plaid.link.linkTokenConfiguration
import com.plaid.link.result.LinkSuccess
import org.json.JSONObject

class Account {
    var id: String = ""
    var owner: String = ""


    fun getPublicToken(
        ctx: Context,
        linkToken: MutableState<String>,
        linkAccountToPlaid: ActivityResultLauncher<LinkTokenConfiguration>
    ) {
        Log.d("TEST","fzrgerzgre")

        // in the below line, we are creating a variable for url.
        var url = "https://bankbackuqac.herokuapp.com/bank/getLink"

        // creating a new variable for our request queue
        val queue = Volley.newRequestQueue(ctx)

        // making a string request to update our data and
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, null,
            Response.Listener { response ->

                val tokenLink = response.get("tokenLink") as String
                //update the view
                linkToken.value = tokenLink

                val linkTokenConfiguration = linkTokenConfiguration {
                    token = tokenLink
                }
                // Launch The connected activity with good linkTokenConfiguration
                linkAccountToPlaid.launch(linkTokenConfiguration)
            },
            Response.ErrorListener { error ->
                // TODO: Handle error
            }
        )
        // below line is to make
        // a json object request.
        queue.add(jsonObjectRequest)
    }

    fun getAccessToken(
        ctx: Context,
        access_token: MutableState<String>,
        publicToken: String
    ) {
        // in the below line, we are creating a variable for url.
        var url = "https://bankbackuqac.herokuapp.com/bank/exchangePktoAccessToken"

        // creating a new variable for our request queue
        val queue = Volley.newRequestQueue(ctx)


        val jsonParams = JSONObject()
        jsonParams.put("publicToken", publicToken)

        // making a string request to update our data and
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonParams,
            Response.Listener { response ->

                val access_tokenValue = response.get("access_token") as String
                //update the view
                access_token.value = access_tokenValue
                Log.d("ACCESS_VAL", access_tokenValue)
            },
            Response.ErrorListener { error ->
                // TODO: Handle error
            }
        )
        // below line is to make
        // a json object request.
        queue.add(jsonObjectRequest)
    }
}