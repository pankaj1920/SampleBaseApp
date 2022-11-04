import com.applaunch.appbase.network_base.NetworkUtils

object BaseMapping {
    fun getBaseUrl(networkUtils: NetworkUtils): String {
        return when (networkUtils) {
            NetworkUtils.BASE_URL -> "https://api.openweathermap.org/data/2.5/"
        }
    }

}

