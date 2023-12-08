package pl.kruszyk.weather.constant

class Const {
    companion object {
        val permissions = arrayOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )

        const val openWeatherMapApiKey = "847800b73b4a772689ee3258530b3746";

        const val colorBg1 = 0xff08203e;
        const val colorBg2 = 0xff557c93;
        const val cardColor = 0xFF333639;

        const val LOADING = "Loading...";
        const val na = "N/A";
    }
}