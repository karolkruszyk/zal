package pl.kruszyk.weather.model.weather

import com.google.gson.annotations.SerializedName

class Weather {
    @SerializedName("id")
    var id: Int? = null
    @SerializedName("main")
    var main: String? = null
    @SerializedName("description")
    var description: String? = null
    @SerializedName("icon")
    var icon: String? = null


}