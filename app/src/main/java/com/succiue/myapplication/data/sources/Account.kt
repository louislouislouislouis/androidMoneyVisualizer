import android.util.Log
import com.squareup.moshi.Json
import com.succiue.myapplication.data.model.AccountModel
import com.succiue.myapplication.data.repository.AccountSource
import retrofit2.Retrofit
import retrofit2.http.POST

object AccountOnlineSource : AccountSource {
    private const val BASE_URL = "https://bankbackuqac.herokuapp.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .build()

    data class SteamAppList(
        @Json(name = "applist")
        val appList: SteamApps
    )

    data class SteamApps(
        @Json(name = "apps")
        val list: List<AccountModel>
    )

    data class Test(
        val accessToken: String
    )

    interface SteamAppsService {
        @POST("bank/getAccessToken")
        suspend fun GetTest(): Test
    }

    private val retrofitService: SteamAppsService by lazy {
        retrofit.create(SteamAppsService::class.java)
    }

    override suspend fun getGames(): List<AccountModel> {
        Log.d("AccountSource", retrofitService.GetTest().toString())
        return listOf()
    }
}
