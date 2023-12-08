package pl.kruszyk.weather.model.forecast

import com.google.gson.annotations.SerializedName
import pl.kruszyk.weather.model.weather.Clouds
import pl.kruszyk.weather.model.weather.Main
import pl.kruszyk.weather.model.weather.Sys
import pl.kruszyk.weather.model.weather.Weather
import pl.kruszyk.weather.model.weather.Wind

data class CustomList(
    @SerializedName("dt") var dt: Int? = null,
    @SerializedName("main") var main: Main? = Main(),
    @SerializedName("weather") var weather: ArrayList<Weather>? = arrayListOf(),
    @SerializedName("clouds") var clouds: Clouds? = Clouds(),
    @SerializedName("wind") var wind: Wind? = Wind(),
    @SerializedName("visibility") var visibility: Int? = null,
    @SerializedName("pop") var pop: Double? = null,
    @SerializedName("sys") var sys: Sys? = null,
    @SerializedName("dt_txt") var dt_txt: String? = null,

    )