import com.squareup.moshi.Json
import com.succiue.myapplication.data.model.AccountModel
import com.succiue.myapplication.data.model.UserModel
import com.succiue.myapplication.data.repository.AccountSource
import retrofit2.Retrofit
import retrofit2.http.GET

object AccountOnlineSource : AccountSource {
    private const val BASE_URL = "http://api.steampowered.com"

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

    interface SteamAppsService {
        @GET("ISteamApps/GetAppList/v0002?format=json")
        suspend fun GetAppList(): SteamAppList
    }

    private val retrofitService: SteamAppsService by lazy {
        retrofit.create(SteamAppsService::class.java)
    }

    override suspend fun getGames(): List<AccountModel> {
        return retrofitService.GetAppList().appList.list.map {
            AccountModel(UserModel("test", "e", "e", "e"))
        }
    }
}
