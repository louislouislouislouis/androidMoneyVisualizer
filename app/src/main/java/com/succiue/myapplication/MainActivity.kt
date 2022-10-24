package com.succiue.myapplication


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.plaid.link.OpenPlaidLink
import com.plaid.link.configuration.LinkTokenConfiguration
import com.plaid.link.linkTokenConfiguration
import com.plaid.link.result.LinkExit
import com.plaid.link.result.LinkSuccess
import com.succiue.myapplication.ui.theme.MyApplicationTheme
import org.json.JSONObject





class MainActivity : ComponentActivity() {
    val publicToken = mutableStateOf("")
    val accessToken = mutableStateOf("")
    val linkToken = mutableStateOf("")

    fun onSuccess(res: LinkSuccess){
        // Send public_token to your server, exchange for access_token
        var publicTokenValue = res.publicToken
        publicToken.value = publicTokenValue
        getAccessToken(applicationContext,accessToken,publicTokenValue)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val linkAccountToPlaid =
            registerForActivityResult(OpenPlaidLink()) {
                when (it) {
                    is LinkSuccess ->  onSuccess(it)
                    is LinkExit -> Log.d("ConnectPlaid", "Error Connecting To Bank Api") /* handle LinkExit */
                }

            }


        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    RandomApp(linkAccountToPlaid,
                              publicToken,
                              accessToken,
                              linkToken)
                }
            }
        }
    }
}

private fun getAccessToken(
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
    val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, jsonParams,
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
private fun getPublicToken(
    ctx: Context,
    linkToken: MutableState<String>,
    linkAccountToPlaid: ActivityResultLauncher<LinkTokenConfiguration>
) {

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

//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RandomApp(
    linkAccountToPlaid: ActivityResultLauncher<LinkTokenConfiguration>,
    publicToken: MutableState<String>,
    accessToken: MutableState<String>,
    linkToken: MutableState<String>,

    ) {

    RandomIntButton(
        Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center),
        linkAccountToPlaid = linkAccountToPlaid,
        publicToken = publicToken,
        accessToken = accessToken,
        linkToken = linkToken
    )
}

@Composable
fun RandomIntButton(modifier: Modifier = Modifier,
                    linkAccountToPlaid: ActivityResultLauncher<LinkTokenConfiguration>,
                    publicToken: MutableState<String>,
                    linkToken: MutableState<String>,
                    accessToken: MutableState<String>
) {
    val ctx = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
        ) {
        Text(text = "link Token " + linkToken.value)
        Text(text = "public Token " + publicToken.value)
        Text(text = "access Token " + accessToken.value)
        Button(onClick = {
            getPublicToken(
                 ctx, linkToken, linkAccountToPlaid
            )
        }) {
            Text(text =  stringResource(R.string.welcoming_user))
        }

    }
}