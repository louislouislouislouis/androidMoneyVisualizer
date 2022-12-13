package com.succiue.myapplication.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.succiue.myapplication.R
import com.succiue.myapplication.data.model.KichtaUserModel
import com.succiue.myapplication.ui.viewmodels.TransactionUiState
import org.json.JSONObject
import java.io.Serializable
import java.util.*
import kotlin.math.roundToInt


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

@Composable
fun getSpendingByMonth(
    listTransaction: List<TransactionUiState>,
    data: MutableMap<String, Float>
) {
    listTransaction.forEach {
        var amount = it.amount.substring(
            0,
            it.amount.length - 1
        ).toFloat()

        var date = it.date.split('/')

        if (amount < 0.0f && date[2] == Calendar.getInstance().get(Calendar.YEAR).toString()) {
            when (date[1]) {
                "01" -> data.put(
                    stringResource(R.string.january),
                    ((data.getValue(stringResource(R.string.january)) - amount) * 100.0f).roundToInt() / 100.0f
                )
                "02" -> data.put(
                    stringResource(R.string.February),
                    ((data.getValue(stringResource(R.string.February)) - amount) * 100.0f).roundToInt() / 100.0f
                )
                "03" -> data.put(
                    stringResource(R.string.March),
                    ((data.getValue(stringResource(R.string.March)) - amount) * 100.0f).roundToInt() / 100.0f
                )
                "04" -> data.put(
                    stringResource(R.string.April),
                    ((data.getValue(stringResource(R.string.April)) - amount) * 100.0f).roundToInt() / 100.0f
                )
                "05" -> data.put(
                    stringResource(R.string.May),
                    ((data.getValue(stringResource(R.string.May)) - amount) * 100.0f).roundToInt() / 100.0f
                )
                "06" -> data.put(
                    stringResource(R.string.June),
                    ((data.getValue(stringResource(R.string.June)) - amount) * 100.0f).roundToInt() / 100.0f
                )
                "07" -> data.put(
                    stringResource(R.string.July),
                    ((data.getValue(stringResource(R.string.July)) - amount) * 100.0f).roundToInt() / 100.0f
                )
                "08" -> data.put(
                    stringResource(R.string.August),
                    ((data.getValue(stringResource(R.string.August)) - amount) * 100.0f).roundToInt() / 100.0f
                )
                "09" -> data.put(
                    stringResource(R.string.September),
                    ((data.getValue(stringResource(R.string.September)) - amount) * 100.0f).roundToInt() / 100.0f
                )
                "10" -> data.put(
                    stringResource(R.string.October),
                    ((data.getValue(stringResource(R.string.October)) - amount) * 100.0f).roundToInt() / 100.0f
                )
                "11" -> data.put(
                    stringResource(R.string.November),
                    ((data.getValue(stringResource(R.string.November)) - amount) * 100.0f).roundToInt() / 100.0f
                )
                "12" -> data.put(
                    stringResource(R.string.December),
                    ((data.getValue(stringResource(R.string.December)) - amount) * 100.0f).roundToInt() / 100.0f
                )
                else -> print("error date format")
            }
        }

    }
}

fun getMaxValue(
    maxValue: Float
): Int {
    if (maxValue < 250) {
        return 250;
    } else if (maxValue < 500) {
        return 500
    } else if (maxValue < 1000) {
        return 1000
    } else if (maxValue < 2000) {
        return 2000
    } else if (maxValue < 5000) {
        return 5000
    } else if (maxValue < 10000) {
        return 10000
    } else if (maxValue < 20000) {
        return 20000
    } else if (maxValue < 50000) {
        return 50000
    } else if (maxValue < 100000) {
        return 100000
    } else return 25000
}


object Constant {
    const val WEB_CLIENT_ID =
        "473823468553-12tebj7jftchaasckr19oveqd8fsos1o.apps.googleusercontent.com"
    const val CODE_GOOGLE_RC_SIGN_IN = 3
    const val CODE_GOOGLE_REQ_ONE_TAP = 2
    const val URL_BANK = "https://www.deploy-w66vlqi-yc3p4hxsi4jhi.ca-1.platformsh.site/"
}

