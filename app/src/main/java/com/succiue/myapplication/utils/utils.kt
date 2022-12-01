package com.succiue.myapplication.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.succiue.myapplication.data.model.KichtaUserModel
import org.json.JSONObject
import java.io.Serializable


fun <T : Serializable?> Intent.getSerializable(key: String, m_class: Class<T>): T {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        this.getSerializableExtra(key, m_class)!!
    else
        this.getSerializableExtra(key) as T

}


inline fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any, R : Any> multipleNonNull(
    p1: T1?,
    p2: T2?,
    p3: T3?,
    p4: T4?,
    block: (T1, T2, T3, T4) -> R?
): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null) block(
        p1,
        p2,
        p3,
        p4
    ) else null
}

inline fun <T1 : Any, T2 : Any, T3 : Any, R : Any> multipleNonNull(
    p1: T1?,
    p2: T2?,
    p3: T3?,
    block: (T1, T2, T3) -> R?
): R? {
    return if (p1 != null && p2 != null && p3 != null) block(
        p1,
        p2,
        p3,
    ) else null
}

fun sendRequest(
    ctx: Context,
    url: String,
    method: Int = Request.Method.GET,
    onSuccess: Response.Listener<org.json.JSONObject>,
    onFailure: Response.ErrorListener,
    owner: KichtaUserModel,
    body: JSONObject? = null,
) {
    // in the below line, we are creating a variable for url.
    //var url = "https://bankbackuqac.herokuapp.com/bank/getLink"

    // creating a new variable for our request queue
    val queue = Volley.newRequestQueue(ctx)

    // making a string request to update our data and
    val jsonObjectRequest = object : JsonObjectRequest(
        method,
        url,
        body,
        onSuccess,
        onFailure
    ) {
        override fun getHeaders(): MutableMap<String, String> {
            val headers = HashMap<String, String>()
            //headers.put("Content-Type", "application/json");
            headers["Authorization"] = "Bearer " + owner.idToken

            return headers
        }
    }
    // below line is to make
    // a json object request.
    queue.add(jsonObjectRequest)
}

object Constant {
    const val WEB_CLIENT_ID =
        "473823468553-12tebj7jftchaasckr19oveqd8fsos1o.apps.googleusercontent.com"
    const val CODE_GOOGLE_RC_SIGN_IN = 3
    const val CODE_GOOGLE_REQ_ONE_TAP = 2
    const val URL_BANK = "https://test-back-24ts.onrender.com/"

}

